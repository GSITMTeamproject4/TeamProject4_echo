package com.project.echoproject.controller;

import com.project.echoproject.dto.order.PaymentRequestDTO;
import com.project.echoproject.dto.order.PaymentResponseDTO;
import com.project.echoproject.service.OrderService;
import com.project.echoproject.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/webhook")
    public ResponseEntity<PaymentResponseDTO> handleWebhook(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentResponseDTO response = paymentService.verifyPayment(paymentRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/complete")
    public RedirectView completePayment(@RequestBody PaymentRequestDTO paymentRequestDTO, RedirectAttributes redirectAttributes) {
        try {
            PaymentResponseDTO response = paymentService.verifyPayment(paymentRequestDTO);
            if (response.isSuccess()) {
                // 결제 완료 페이지로 리디렉션하며 정보를 전달
                redirectAttributes.addFlashAttribute("paymentResponse", response);
                return new RedirectView("/payment/orderSuccess");
            } else {
                // 결제 실패 페이지로 리디렉션하며 정보를 전달
                redirectAttributes.addFlashAttribute("paymentResponse", response);
                return new RedirectView("/payment/orderFailure");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("paymentResponse", new PaymentResponseDTO(false, e.getMessage()));
            return new RedirectView("/payment/orderFailure");
        }
    }

    @GetMapping("/orderSuccess")
    public String getOrderSuccess(Model model) {
        // 결제 완료 페이지로 이동
        // model에 이미 paymentResponse가 포함되어 있음
        return "orderSuccess";
    }

    @GetMapping("/orderFailure")
    public String getOrderFailure(Model model) {
        // 결제 실패 페이지로 이동
        return "orderFailure";
    }
}
