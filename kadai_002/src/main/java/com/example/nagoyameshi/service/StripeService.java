package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;

@Service
public class StripeService {

    public void processSessionCompleted(Event event) {
        // EventからSessionを取得
        Optional<StripeObject> optionalObject = event.getDataObjectDeserializer().getObject();

        optionalObject.ifPresent(stripeObject -> {
            Session session = (Session) stripeObject;
            
            // ここでsessionから顧客メールやサブスクリプションID、メタデータなどを取り出し、DBに保存するロジックを実装
            String subscriptionId = session.getSubscription();
            String customerEmail = session.getCustomerEmail();

            // 例: サブスクリプションIDやメールアドレスを用いて、ユーザーのロールを有料会員に変更
            // ※実際にはUserRepository等を使ってDB更新
            System.out.println("Subscription ID: " + subscriptionId);
            System.out.println("Customer Email: " + customerEmail);

            // 必要に応じてメタデータ取得など
            // Map<String, String> metadata = session.getMetadata();
            // reservationId = metadata.get("reservationId");
            // reservationService.create(...); // このように別のサービスを呼び出すことも可能
        });
    }
}
