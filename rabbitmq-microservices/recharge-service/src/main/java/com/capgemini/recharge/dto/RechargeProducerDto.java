package com.capgemini.recharge.dto;



public class RechargeProducerDto {
	
	private String mobileNumber;
	private Double amount;
	private String operator;
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public RechargeProducerDto(String mobileNumber, Double amount, String operator) {
		super();
		this.mobileNumber = mobileNumber;
		this.amount = amount;
		this.operator = operator;
	}
	public RechargeProducerDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
