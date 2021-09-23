package com.otsi.retail.newSale.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.UserData;
import com.otsi.retail.newSale.Entity.UserDataAv;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.common.UserDataAVEnum;
import com.otsi.retail.newSale.mapper.CustomerMapper;
import com.otsi.retail.newSale.repository.CustomerDetailsRepo;
import com.otsi.retail.newSale.repository.UserDataAvRepo;
import com.otsi.retail.newSale.repository.UserDataRepo;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.UserDataVo;

@Service
public class CustomerServiceImpl implements CustomerService {

	private Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private UserDataRepo userDataRepo;

	@Autowired
	private UserDataAvRepo userDataAvRepo;

	@Override
	public String  saveCustomerDetails(CustomerVo details) throws DuplicateRecordException {
		log.debug("deugging saveCustomerDetails:" + details);
		
			Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

			if (!list.isPresent()) {

				CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);// mapper for VO to DTO
				// details = customerMapper.convertEntityToVo(customerRepo.save(entity));
				CustomerDetailsEntity savedDetails = customerRepo.save(entity);
				log.warn("we are testing customer is saved succesfully..");
				log.info("customer details saved succesfully..." + details);
				return "Customer details saved successfully..";
			} else {
				log.error("Mobile number is already in my records");
				throw new DuplicateRecordException("Mobile number is already in my records") ;
			}
		
	}

	@Override
	public CustomerVo getCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundExcecption {
		log.debug("deugging getCustomerByMobileNumber:" + mobileNumber);
		Optional<CustomerDetailsEntity> customerDetails = customerRepo.findByMobileNumber(mobileNumber);
		if (!customerDetails.isPresent()) {
			log.error(
					"Customer is with mobile number " + mobileNumber + " not exists. So please fill all the details..");
			throw new CustomerNotFoundExcecption("Customer is with mobile number " + mobileNumber
					+ " not exists. So please fill all the details.." + HttpStatus.BAD_REQUEST);
		} else {
			CustomerVo details = customerMapper.convertEntityToVo(customerDetails.get());
			log.warn("we are testing getting customer by mobile number");
			log.info("after getting CustomerByMobileNumber :" + details);
			return details;

		}
	}

	// Method for saving user data
	@Override
	public String saveUserData(UserDataVo vo) throws DuplicateRecordException {

		if (vo.getPhoneNumber() != null) {
			Optional<UserData> user = userDataRepo.findByPhoneNumber(vo.getPhoneNumber());

			if (user.isPresent()) {
				throw new DuplicateRecordException("User already exits with Mobile Number : " + user.get().getPhoneNumber());
			}
		}

		UserData entity = new UserData();

		// Set common fields of User data
		entity.setUserName(vo.getUserName());
		entity.setGender(vo.getGender());
		entity.setPhoneNumber(vo.getPhoneNumber());
		entity.setCreationdate(LocalDate.now());
		entity.setLastmodified(LocalDate.now());

		UserData savedUser = userDataRepo.save(entity);
		// Method for saving extra attributes in User data Av table
		saveAVValues(vo, savedUser);
		return "User data saved successfully..";
	}

	// Method for saving extra attributes in Attribute value table
	private void saveAVValues(UserDataVo vo, UserData savedUser) {

		UserDataAv userAv = null;
		// Set GST Number
		if (vo.getGstNumber() != null) {
			userAv = new UserDataAv();
			userAv.setName(UserDataAVEnum.GSTNUMBER.geteName());
			userAv.setType(UserDataAVEnum.GSTNUMBER.getId());
			userAv.setStringValue(vo.getGstNumber());
			saveToRepo(userAv, savedUser);

		}
		// Set PAN Number
		if (vo.getPanNumber() != null) {

			userAv = new UserDataAv();
			userAv.setName(UserDataAVEnum.PANNUMBER.geteName());
			userAv.setType(UserDataAVEnum.PANNUMBER.getId());
			userAv.setStringValue(vo.getPanNumber());
			saveToRepo(userAv, savedUser);
		}
		// Set Date of birth(DOB)
		if (vo.getDob() != null) {

			userAv = new UserDataAv();
			userAv.setName(UserDataAVEnum.DOB.geteName());
			userAv.setType(UserDataAVEnum.DOB.getId());
			userAv.setDateValue(vo.getDob());
			saveToRepo(userAv, savedUser);

		}
		// Set email
		if (vo.getEmail() != null) {

			userAv = new UserDataAv();
			userAv.setName(UserDataAVEnum.EMAIL.geteName());
			userAv.setType(UserDataAVEnum.EMAIL.getId());
			userAv.setStringValue(vo.getEmail());
			saveToRepo(userAv, savedUser);

		}
		// Set Address
		if (vo.getAddress() != null) {

			userAv = new UserDataAv();
			userAv.setName(UserDataAVEnum.ADDRESS.geteName());
			userAv.setType(UserDataAVEnum.ADDRESS.getId());
			userAv.setStringValue(vo.getAddress());
			saveToRepo(userAv, savedUser);

		}
	}

	// Method for calling User data Av repository
	private void saveToRepo(UserDataAv userAv, UserData savedUser) {

		userAv.setLastModified(LocalDate.now());
		userAv.setUserData(savedUser);
		userDataAvRepo.save(userAv);
	}

	// Method for fetching User data by using mobile Number
	@Override
	public UserDataVo  getUserByMobileNo(Long mobileNum) throws RecordNotFoundException {

		Optional<UserData> userDetails = userDataRepo.findByPhoneNumber(mobileNum);
		UserDataVo vo = new UserDataVo();
		if (userDetails.isPresent()) {
			UserData user = userDetails.get();

			vo.setUserId(user.getUserId());
			vo.setUserName(user.getUserName());
			vo.setGender(user.getGender());
			vo.setPhoneNumber(user.getPhoneNumber());
			vo.setCreationdate(user.getCreationdate());

			user.getUserAv().stream().forEach(x -> {
				if (x.getName().equalsIgnoreCase(UserDataAVEnum.GSTNUMBER.geteName())) {
					vo.setGstNumber(x.getStringValue());
				}
				if (x.getName().equalsIgnoreCase(UserDataAVEnum.PANNUMBER.geteName())) {
					vo.setPanNumber(x.getStringValue());
				}
				if (x.getName().equalsIgnoreCase(UserDataAVEnum.DOB.geteName())) {
					vo.setDob(x.getDateValue());
				}
				if (x.getName().equalsIgnoreCase(UserDataAVEnum.EMAIL.geteName())) {
					vo.setEmail(x.getStringValue());
				}

			});

		} else {
			throw new RecordNotFoundException("User not found with mobile number " + mobileNum);
		}

		return vo;
	}

}
