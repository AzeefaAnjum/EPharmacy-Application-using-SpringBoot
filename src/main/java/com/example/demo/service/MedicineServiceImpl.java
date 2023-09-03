package com.example.demo.service;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MedicineDTO;
import com.example.demo.entity.Medicine;
import com.example.demo.exception.EpharmacyException;
import com.example.demo.repository.MedicineRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {
	@Autowired
	MedicineRepository medicineRepository;
	@Override
	public List<MedicineDTO> getAllMedicines() throws EpharmacyException {
		// TODO Auto-generated method stub
		Iterable<Medicine> listofiterable=medicineRepository.findAll();
		List<MedicineDTO> listofdata=new ArrayList<>();
		for (Medicine medicine : listofiterable) {
			MedicineDTO medicinedto=new MedicineDTO();
			medicinedto.setQuantity(medicine.getQuantity());
			medicinedto.setDiscountPercent(medicine.getDiscountPercent());
			medicinedto.setExpiryDate(medicine.getExpiryDate());
			medicinedto.setCategory(medicine.getCategory());
			medicinedto.setManufacturer(medicine.getCategory());
			medicinedto.setManufacturingDate(medicine.getManufacturingDate());
			medicinedto.setMedicineId(medicine.getMedicineId());
			medicinedto.setMedicineName(medicine.getMedicineName());
			medicinedto.setQuantity(medicine.getQuantity());
			medicinedto.setPrice(medicine.getPrice());
			listofdata.add(medicinedto);
		}
		if(listofdata.isEmpty())	return Collections.emptyList();
		return listofdata;
	}

	@Override
	public List<MedicineDTO> getMedicinesByCategory(String category) throws EpharmacyException {
		// TODO Auto-generated method stub
		Iterable<Medicine> listofiterable=medicineRepository.findByCategory(category);
		List<MedicineDTO> listofdata=new ArrayList<>();
		for (Medicine medicine : listofiterable) {
			MedicineDTO medicinedto=new MedicineDTO();
			medicinedto.setQuantity(medicine.getQuantity());
			medicinedto.setDiscountPercent(medicine.getDiscountPercent());
			medicinedto.setExpiryDate(medicine.getExpiryDate());
			medicinedto.setCategory(medicine.getCategory());
			medicinedto.setManufacturer(medicine.getCategory());
			medicinedto.setManufacturingDate(medicine.getManufacturingDate());
			medicinedto.setMedicineId(medicine.getMedicineId());
			medicinedto.setMedicineName(medicine.getMedicineName());
			medicinedto.setQuantity(medicine.getQuantity());
			medicinedto.setPrice(medicine.getPrice());
			listofdata.add(medicinedto);
		}
		if(listofdata.isEmpty())	return Collections.emptyList();
		return listofdata;
	}

	@Override
	public MedicineDTO getMedicineById(Integer medicineId) throws EpharmacyException {
		// TODO Auto-generated method stub
		Optional<Medicine> data=medicineRepository.findById(medicineId);
		Medicine medicine =data.orElseThrow(()->new EpharmacyException("Medicine Not Found"));
		MedicineDTO medicinedto=new MedicineDTO();
		medicinedto.setQuantity(medicine.getQuantity());
		medicinedto.setDiscountPercent(medicine.getDiscountPercent());
		medicinedto.setExpiryDate(medicine.getExpiryDate());
		medicinedto.setCategory(medicine.getCategory());
		medicinedto.setManufacturer(medicine.getCategory());
		medicinedto.setManufacturingDate(medicine.getManufacturingDate());
		medicinedto.setMedicineId(medicine.getMedicineId());
		medicinedto.setMedicineName(medicine.getMedicineName());
		medicinedto.setQuantity(medicine.getQuantity());
		medicinedto.setPrice(medicine.getPrice());
		return medicinedto;
	}

	@Override
	public String updateMedicineQuantityAfterOrder(Integer medicineId, Integer orderedQuantity)
			throws EpharmacyException {
		Optional<Medicine> data=medicineRepository.findById(medicineId);
		Medicine medicine =data.orElseThrow(()->new EpharmacyException("Medicine Not Found"));
		medicine.setQuantity(medicine.getQuantity()-orderedQuantity);
		// TODO Auto-generated method stub
		return "Medicine quantity updated successfully";
	}

}