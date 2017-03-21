package com.iugu;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.iugu.auth.Authenticator;
import com.iugu.services.CustomerService;
import com.iugu.services.InvoiceService;
import com.iugu.services.PlanService;
import com.iugu.services.SubscriptionService;

/*
 * Iugu
 * Main Class
 */
public class Iugu {
	private static final String URLBase = "https://api.iugu.com/v1";
	private static Iugu unsafeIugu = null;
	private final Services services = new Services();
	private final String tokenId;
	private Client client = null;
	
	/*
	 * Initialize a thread safe implementation
	 */
	private Iugu(final String Token) {
		tokenId = Token;
	}
	public static Iugu spawn(final String ClientToken) {
		return new Iugu(ClientToken);
	}
	// Get the client, thread safe
	public Client getClient() {
		if(client == null) {
			client = ClientBuilder.newClient().register(new Authenticator(tokenId, ""));
		}
		
		return client;
	}
	// Get the services that are available
	public Services services() {
		return services;
	}
	
	/*
	 * Initialize a thread unsafe implementation
	 */
	public static void init(final String Token) {
		unsafeIugu = new Iugu(Token);
		
	}
	// Gets a statical Global Config
	public static Iugu get() {
		return unsafeIugu;
	}
	
	/*
	 * Auxiliary
	 */
	public static String url(String endpoint) {
		return URLBase + endpoint;
	}
	
	public class Services {
		private CustomerService customer = null;
		private InvoiceService invoice = null;
		private PlanService plan = null;
		private SubscriptionService subscription = null;
		
		public CustomerService customer() {
			if(customer==null)
				customer = new CustomerService(Iugu.this);
			
			return customer;
		}
		public InvoiceService invoice() {
			if(invoice==null)
				invoice = new InvoiceService(Iugu.this);
			
			return invoice;
		}
		public PlanService plan() {
			if(plan==null)
				plan = new PlanService(Iugu.this);
			
			return plan;
		}
		public SubscriptionService subscription() {
			if(subscription==null)
				subscription = new SubscriptionService(Iugu.this);
			
			return subscription;
		}
	}
}