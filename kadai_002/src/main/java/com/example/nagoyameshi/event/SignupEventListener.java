package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.VerificationTokenService;

// ListenerクラスのインスタンスがDIコンテナに登録されるようにする
@Component
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
		System.out.println("SignupEventListenerが初期化されました。");
	}
	
	// Listenerクラスないでは、イベント発生時に実行したいメソッドに対して@EventListenerアノテーションをつける
	@EventListener
	/* どのイベントの発生時かを指定するために通知を受け取るEventクラスをメソッドの引数に設定する
	 今回の例であればSignupEventクラスから通知を受けた時にonsignupEventメソッドが実行される*/
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString();
		verificationTokenService.create(user, token);
		
		String recipientAddress = user.getMailAddress();
		String subject = "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックして会員登録を完了してください。";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		
		try {
			// javaMailSenderインターフェースを使用してメールを送信する
			javaMailSender.send(mailMessage);
			System.out.println("メールが正常に送信されました");
		} catch (Exception e) {
			System.err.println("メール送信中にエラーが発生しました：" + e.getMessage());
			e.printStackTrace();
		}
	}
}
