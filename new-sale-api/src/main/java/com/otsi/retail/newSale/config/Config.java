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

	@Value("${newsale_queue}")
	private String newSaleQueue;

	@Value("${newsale_exchange}")
	private String newSaleExchange;

	@Value("${payment_newsale_rk}")
	private String paymentNewsaleRK;

	@Value("${inventory_queue_retail}")
	private String inventoryRetailQueue;

	@Value("${inventory_retail_exchange}")
	private String inventoryRetailExchange;

	@Value("${inventory_retail_rk}")
	private String inventoryRetailRK;

	@Value("${inventory_queue_textile}")
	private String inventoryUpdateQueueTextile;

	@Value("${inventory_exchange}")
	private String updateInventoryExchangeTextile;

	@Value("${payment_newsale_rk}")
	private String updateInventoryTextileRK;
	

	@Qualifier("inventoryUpdateQueueTextile")
	public Queue inventoryUpdateQueueTextile() {
		return new Queue(inventoryUpdateQueueTextile);
	}

	@Qualifier("updateInventoryExchangeTextile")
	public DirectExchange updateInventoryExchangeTextile() {
		return new DirectExchange(updateInventoryExchangeTextile);
	}

	@Bean
	public Binding bindingUpdateInventoryTextile(Queue inventoryUpdateQueueTextile,
			DirectExchange updateInventoryExchangeTextile) {

		return BindingBuilder.bind(inventoryUpdateQueueTextile).to(updateInventoryExchangeTextile)
				.with(updateInventoryTextileRK);
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

	@Qualifier("inventoryRetailQueue")
	public Queue inventoryUpdateQueueRetail() {
		return new Queue(inventoryRetailQueue);
	}

	@Qualifier("inventoryRetailExchange")
	public DirectExchange directExchangeForRetail() {
		return new DirectExchange(inventoryRetailExchange);
	}

	@Bean
	public Binding bindingForRetail(Queue inventoryRetailQueue, DirectExchange inventoryRetailExchange) {

		return BindingBuilder.bind(inventoryRetailQueue).to(inventoryRetailExchange).with(inventoryRetailRK);
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
