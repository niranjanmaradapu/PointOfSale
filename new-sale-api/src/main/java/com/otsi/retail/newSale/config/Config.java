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

	@Value("${savecustomer_url}")
	private String url;

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
	
	@Value("${updateCreditNotesDetails_url}")
	private String updateCreditNotesDetails;
	
	
	//For Payment RabbitMQ Config

	@Value("${newsale_queue}")
	private String newSaleQueue;

	@Value("${newsale_exchange}")
	private String newSaleExchange;

	@Value("${payment_newsale_rk}")
	private String paymentNewsaleRK;
	
	//For Inventory Config

	@Value("${inventory_queue}")
	private String inventoryUpdateQueue;

	@Value("${inventory_exchange}")
	private String updateInventoryExchange;

	@Value("${inventory_rk}")
	private String updateInventoryRK;
	
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
