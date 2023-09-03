package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.entity.Card;
import com.example.demo.entity.Payment;
import com.example.demo.exception.EpharmacyException;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.utility.HashingUtility;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{

	@Autowired
	private CardRepository cardRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Override
	public Integer makePayment(String cardId, Double amountToPay) throws EpharmacyException {
		// TODO Auto-generated method stub
		Optional<Card> optional = cardRepository.findById(cardId);
		Card card = optional.orElseThrow(()->new EpharmacyException("Card Not found"));
		Payment payment = new Payment();
		payment.setCustomerEmailId(card.getCustomerEmailId());
		payment.setAmount(amountToPay);
		payment.setPaymentTime(LocalDateTime.now());
		payment.setCardId(card.getCardId());
		paymentRepository.save(payment);
		return payment.getPaymentId();
		
	}

	@Override
	public String addCard(CardDTO cardDTO) throws Exception {
		// TODO Auto-generated method stub
		Card c = new Card();
		c.setCardId(cardDTO.getCardId());
		c.setNameOnCard(cardDTO.getNameOnCard());
		c.setCvv(HashingUtility.getHashValue(cardDTO.getCvv()));
		c.setExpiryDate(cardDTO.getExpiryDate());
		c.setCardType(cardDTO.getCardType());
		c.setCustomerEmailId(cardDTO.getCustomerEmailId());
		cardRepository.save(c);
		return "Card Added Successfully";
	}

	@Override
	public String deleteCard(String cardId) throws EpharmacyException {
		// TODO Auto-generated method stub
		Optional<Card> optional=cardRepository.findById(cardId);
		Card carddata =optional.orElseThrow(()->new EpharmacyException("Card Not found"));
		cardRepository.delete(carddata);
		return "Deleted Successfully";
	}

	@Override
	public List<CardDTO> viewCards(String email) throws EpharmacyException {
		// TODO Auto-generated method stub
		Iterable<Card> iterable=cardRepository.findByCustomerEmailId(email);
		List<CardDTO> cardDTOS = new ArrayList<>();
		for (Card card : iterable) {
			CardDTO dto = new CardDTO();
			dto.setCardId(card.getCardId());
			dto.setCardType(card.getCardType());
			dto.setCustomerEmailId(card.getCustomerEmailId());
			dto.setCvv(card.getCvv());
			dto.setExpiryDate(card.getExpiryDate());
			dto.setNameOnCard(card.getNameOnCard());
			cardDTOS.add(dto);	
		}
		return cardDTOS;
	}

	@Override
	public PaymentDTO getPaymentDetails(Integer paymentId) throws EpharmacyException {
		// TODO Auto-generated method stub
		
		Optional<Payment> optional=paymentRepository.findById(paymentId);
		Payment payment= optional.orElseThrow(()->new EpharmacyException("PAYMENT ID DOES NOT EXSIST"));
		PaymentDTO paymenDTO = new PaymentDTO();
		paymenDTO.setAmount(payment.getAmount());
		paymenDTO.setCustomerEmailId(payment.getCustomerEmailId());
		paymenDTO.setPaymentId(payment.getPaymentId());
		paymenDTO.setPaymentTime(payment.getPaymentTime());
		
		return paymenDTO;

	}

	@Override
	public CardDTO getCardDetails(String cardId) throws EpharmacyException {
		// TODO Auto-generated method stub
		Optional<Card> optional = cardRepository.findById(cardId);
		Card carddata =optional.orElseThrow(()->new EpharmacyException("Card Not found"));
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(carddata.getCardId());
		cardDTO.setNameOnCard(carddata.getNameOnCard());
		cardDTO.setCvv(carddata.getCvv());
		cardDTO.setExpiryDate(carddata.getExpiryDate());
		cardDTO.setCardType(carddata.getCardType());
		cardDTO.setCustomerEmailId(carddata.getCustomerEmailId());
		return cardDTO;
	}

	
}
