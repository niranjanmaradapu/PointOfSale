package com.otsi.retail.newSale.config;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.otsi.retail.newSale.vo.HsnDetailsVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

	public List<HsnDetailsVo> vo;

<<<<<<< HEAD
=======
	

>>>>>>> Release-2
	@Value("${getNewSaleWithHsn_url}")
	private String HsnUrl;

	@Value("${getreturnslip_url}")
	private String getListOfReturnSlips;

	@Value("${getreturnslips_url}")
	private String getAllListOfReturnSlips;

	@Value("${getCustomerDetailsFromURM_url}")
	private String getCustomerDetailsFromURM;

	@Value("${inventoryUpdateTextile_url}")
	private String inventoryUpdateForTextile;

	@Value("${inventoryUpdateRetail_url}")
	private String inventoryUpdateForRetail;
	
	@Value("${updateAccounting_url}")
	private String updateAccountingDetails;
	
	
	//For Payment RabbitMQ Config

	@Value("${newsale_queue}")
	private String newSaleQueue;

	@Value("${newsale_exchange}")
	private String newSaleExchange;

	@Value("${payment_newsale_rk}")
	private String paymentNewsaleRK;
	
	//accounting queue
	
	@Value("${accounting_queue}")
	private String accountingQueue;
	
	@Value("${accounting_exchange}")
	private String accountingExchange;
	
	@Value("${accounting_rk}")
	private String accountingRK;
	
	//For Inventory Config

	@Value("${inventory_queue}")
	private String inventoryUpdateQueue;

	@Value("${inventory_exchange}")
	private String updateInventoryExchange;

	@Value("${inventory_rk}")
	private String updateInventoryRK;
	
	//ineventory update for returnslip
	
	@Value("${returnslip_queue}")
	private String returnSlipinventoryUpdateQueue;

	@Value("${returnslip_exchange}")
	private String returnSlipupdateInventoryExchange;

	@Value("${returnslip_rk}")
	private String returnSlipupdateInventoryRK;
	
	//creditNotesSave
	@Value("${return_credit_queue}")
	private String creditNotesQueue;

	@Value("${return_credit_exchange}")
	private String creditNotesExchange;

	@Value("${return_credit_rk}")
	private String creditnotesRK;
	
	
	
	@Value("${getStoreDetails_url}")
	private String storeDetails;
	
	@Value("${getUserDetails_url}")
	private String userDetails;
	
	@Value("${getCustomerDetails_url}")
	private String customerDetails;
	
	@Bean
	public Queue inventoryUpdateQueue() {
		return new Queue(inventoryUpdateQueue);
	}

	@Bean
	public DirectExchange updateInventoryExchange() {
		return new DirectExchange(updateInventoryExchange);
	}

	@Bean
	public Binding bindingUpdateInventory(Queue inventoryUpdateQueue,
			DirectExchange updateInventoryExchange) {

		return BindingBuilder.bind(inventoryUpdateQueue).to(updateInventoryExchange)
				.with(updateInventoryRK);
	}
	
	
	@Bean
	public Queue accountingQueue() {
		return new Queue(accountingQueue);
	}
	
	@Bean
	public DirectExchange accountingExchange() {
		return new DirectExchange(accountingExchange);
	}


	@Bean
	public Binding bindingAccounting(Queue accountingQueue, DirectExchange accountingExchange) {

		return BindingBuilder.bind(accountingQueue).to(accountingExchange).with(accountingRK);
	}
	
	
	


	@Bean
	public Queue returnSlipinventoryUpdateQueue() {
		return new Queue(returnSlipinventoryUpdateQueue);
	}

	@Bean
	public DirectExchange returnSlipupdateInventoryExchange() {
		return new DirectExchange(returnSlipupdateInventoryExchange);
	}

	@Bean
	public Binding bindingReturnslipUpdateInventory(Queue returnSlipinventoryUpdateQueue,
			DirectExchange returnSlipupdateInventoryExchange) {

		return BindingBuilder.bind(returnSlipinventoryUpdateQueue).to(returnSlipupdateInventoryExchange)
				.with(returnSlipupdateInventoryRK);
	}
	
	@Bean
	public Queue creditNotesQueue() {
		return new Queue(creditNotesQueue);
	}

	@Bean
	public DirectExchange creditNotesExchange() {
		return new DirectExchange(creditNotesExchange);
	}

	@Bean
	public Binding bindingcreditnotes(Queue creditNotesQueue,
			DirectExchange creditNotesExchange) {

		return BindingBuilder.bind(creditNotesQueue).to(creditNotesExchange)
				.with(creditnotesRK);
	}
	
	
	
	
	@Bean
	public Queue queue() {
		return new Queue(newSaleQueue);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(newSaleExchange);
	}

	@Bean
	public Binding binding(Queue queue, DirectExchange directExchange) {

		return BindingBuilder.bind(queue).to(directExchange).with(paymentNewsaleRK);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory factory) {

		RabbitTemplate template = new RabbitTemplate(factory);
		template.setMessageConverter(messageConverter());
		return template;

	}
	

}
