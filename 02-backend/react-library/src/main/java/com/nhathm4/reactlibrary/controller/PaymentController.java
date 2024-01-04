package com.nhathm4.reactlibrary.controller;

import com.nhathm4.reactlibrary.configvnpay.Config;
import com.nhathm4.reactlibrary.dto.PaymentResDTO;
import com.nhathm4.reactlibrary.service.PaymentService;
import com.nhathm4.reactlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PutMapping("/payment_complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null){
            throw  new Exception("Useremail  is missing !!!");
        }
        return paymentService.stripePayment(userEmail);
    }



    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestParam("amount") long amount) throws UnsupportedEncodingException {

        PaymentResDTO paymentResDTO = paymentService.createPayment(amount);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
    }



}
