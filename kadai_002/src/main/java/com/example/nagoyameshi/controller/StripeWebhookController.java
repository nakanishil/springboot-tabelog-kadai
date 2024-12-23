package com.example.nagoyameshi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret; // StripeダッシュボードでWebhook設定時に取得

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public StripeWebhookController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");

        // Payload取得
        try (BufferedReader reader = request.getReader()) {
            payload = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println("Error reading request payload: " + e.getMessage());
            return ResponseEntity.badRequest().body("Unable to read request body");
        }

        // Webhook検証と処理
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Webhook verified: " + event.getType());

            // イベントタイプごとの処理
            switch (event.getType()) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    break;
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    break;
                default:
                    // 未定義イベントのログ出力
                    System.out.println("Unhandled event type: " + event.getType());
                    break;
            }
        } catch (SignatureVerificationException e) {
            System.err.println("Webhook signature verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Error parsing event: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        return ResponseEntity.ok("Success");
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) {
            System.err.println("Session is null");
            return;
        }

        String customerEmail = session.getCustomerEmail();
        if (customerEmail == null) {
            System.out.println("Customer Email is null. Attempting to retrieve Customer ID.");
            String customerId = session.getCustomer();
            if (customerId != null) {
                try {
                    Customer customer = Customer.retrieve(customerId);
                    customerEmail = customer.getEmail();
                    System.out.println("Retrieved Customer Email: " + customerEmail);
                } catch (Exception e) {
                    System.err.println("Failed to retrieve customer details: " + e.getMessage());
                    return;
                }
            }
        }

        if (customerEmail == null) {
            System.err.println("Customer Email is still null after retrieval attempt.");
            return;
        }

        User user = userRepository.findByMailAddress(customerEmail);
        if (user == null) {
            System.err.println("User not found for email: " + customerEmail);
            return;
        }

        // 更新処理
        Role payingRole = roleRepository.findByName("ROLE_PAYING_MEMBER");
        user.setRole(payingRole);
        user.setSubscriptionId(session.getSubscription());
        userRepository.save(user);

        System.out.println("User updated for email: " + customerEmail);
    }

    private void handlePaymentIntentSucceeded(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            System.err.println("Payment Intent is null");
            return;
        }

        System.out.println("Payment Intent ID: " + paymentIntent.getId());
        System.out.println("Amount Received: " + paymentIntent.getAmountReceived());
    }
}
