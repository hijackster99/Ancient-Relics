package com.hijackster99.core;

public class Request {

	private RelayType type;
	private RequestType reqType;
	private int amount;
	private INetwork receiver;
	private INetwork prevPull;
	
	public Request(RelayType type, RequestType reqType, int amount, INetwork receiver, INetwork prevPull) {
		this.type = type;
		this.amount = amount;
		this.receiver = receiver;
		this.prevPull = prevPull;
		this.reqType = reqType;
	}

	public RelayType getType() {
		return type;
	}

	public void setType(RelayType type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public INetwork getReceiver() {
		return receiver;
	}

	public void setReceiver(INetwork receiver) {
		this.receiver = receiver;
	}

	public INetwork getPrevPull() {
		return prevPull;
	}

	public void setPrevPull(INetwork prevPull) {
		this.prevPull = prevPull;
	}
	
	public RequestType getReqType() {
		return reqType;
	}

}
