package com.hijackster99.core;

public interface INetwork extends IVoid{
	
	//public void sendPullRequest(Network.RelayType type, int amount);
	
	//public int completePullRequest(Network.RelayType type, int amount);
	
	public void makeRequest(IVoid iv, Request req);
	
	boolean inVoid();
	boolean outVoid();
	
	void addConnection(INetwork n);
	void removeConnection(INetwork n);
	void removeFromNetwork();

	void addInVoid(IVoid n);
	void addOutVoid(IVoid n);

	void removeInVoid(IVoid n);
	void removeOutVoid(IVoid n);
	
	boolean hasRequest(Request n);
	
	IVoid getReceiver(Request req);
	
}
