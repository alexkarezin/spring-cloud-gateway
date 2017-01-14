package org.springframework.cloud.gateway.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.actuate.GatewayEndpoint;
import org.springframework.cloud.gateway.api.RouteReader;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.filter.route.AddRequestHeaderRouteFilter;
import org.springframework.cloud.gateway.filter.route.AddResponseHeaderRouteFilter;
import org.springframework.cloud.gateway.filter.route.RemoveRequestHeaderRouteFilter;
import org.springframework.cloud.gateway.filter.route.RemoveResponseHeaderRouteFilter;
import org.springframework.cloud.gateway.filter.route.RewritePathRouteFilter;
import org.springframework.cloud.gateway.filter.route.RouteFilter;
import org.springframework.cloud.gateway.filter.route.SetPathRouteFilter;
import org.springframework.cloud.gateway.filter.route.SetResponseHeaderRouteFilter;
import org.springframework.cloud.gateway.filter.route.SetStatusRouteFilter;
import org.springframework.cloud.gateway.handler.GatewayFilteringWebHandler;
import org.springframework.cloud.gateway.handler.GatewayPredicateHandlerMapping;
import org.springframework.cloud.gateway.handler.GatewayWebHandler;
import org.springframework.cloud.gateway.handler.predicate.CookiePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.HeaderPredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.HostPredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.MethodPredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.QueryPredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.UrlPredicateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableConfigurationProperties
public class GatewayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WebClient webClient() {
		return WebClient.builder(new ReactorClientHttpConnector()).build();
	}

	@Bean
	public RouteToRequestUrlFilter findRouteFilter() {
		return new RouteToRequestUrlFilter();
	}

	@Bean
	@ConditionalOnMissingBean(RouteReader.class)
	public PropertiesRouteReader propertiesRouteReader(GatewayProperties properties) {
		return new PropertiesRouteReader(properties);
	}

	@Bean
	public GatewayProperties gatewayProperties() {
		return new GatewayProperties();
	}

	@Bean
	public GatewayWebHandler gatewayController(WebClient webClient) {
		return new GatewayWebHandler(webClient);
	}

	@Bean
	public GatewayFilteringWebHandler gatewayFilteringWebHandler(GatewayWebHandler gatewayWebHandler,
																 List<GatewayFilter> filters,
																 Map<String, RouteFilter> filterDefinitions) {
		return new GatewayFilteringWebHandler(gatewayWebHandler, filters, filterDefinitions);
	}

	// Predicate beans

	@Bean
	public GatewayPredicateHandlerMapping gatewayPredicateHandlerMapping(GatewayFilteringWebHandler webHandler,
																		 Map<String, PredicateFactory> predicates,
																		 RouteReader routeReader) {
		return new GatewayPredicateHandlerMapping(webHandler, predicates, routeReader);
	}

	@Bean(name = "CookiePredicateFactory")
	public CookiePredicateFactory cookiePredicateFactory() {
		return new CookiePredicateFactory();
	}

	@Bean(name = "HeaderPredicateFactory")
	public HeaderPredicateFactory headerPredicateFactory() {
		return new HeaderPredicateFactory();
	}

	@Bean(name = "HostPredicateFactory")
	public HostPredicateFactory hostPredicateFactory() {
		return new HostPredicateFactory();
	}

	@Bean(name = "MethodPredicateFactory")
	public MethodPredicateFactory methodPredicateFactory() {
		return new MethodPredicateFactory();
	}

	@Bean(name = "QueryPredicateFactory")
	public QueryPredicateFactory queryPredicateFactory() {
		return new QueryPredicateFactory();
	}

	@Bean(name = "UrlPredicateFactory")
	public UrlPredicateFactory urlPredicateFactory() {
		return new UrlPredicateFactory();
	}

	// Filter Factory beans

	@Bean(name = "AddRequestHeaderRouteFilter")
	public AddRequestHeaderRouteFilter addRequestHeaderRouteFilter() {
		return new AddRequestHeaderRouteFilter();
	}

	@Bean(name = "AddResponseHeaderRouteFilter")
	public AddResponseHeaderRouteFilter addResponseHeaderRouteFilter() {
		return new AddResponseHeaderRouteFilter();
	}

	@Bean(name = "RemoveRequestHeaderRouteFilter")
	public RemoveRequestHeaderRouteFilter removeRequestHeaderRouteFilter() {
		return new RemoveRequestHeaderRouteFilter();
	}

	@Bean(name = "RemoveResponseHeaderRouteFilter")
	public RemoveResponseHeaderRouteFilter removeResponseHeaderRouteFilter() {
		return new RemoveResponseHeaderRouteFilter();
	}

	@Bean(name = "RewritePathRouteFilter")
	public RewritePathRouteFilter rewritePathRouteFilter() {
		return new RewritePathRouteFilter();
	}

	@Bean(name = "SetPathRouteFilter")
	public SetPathRouteFilter setPathRouteFilter() {
		return new SetPathRouteFilter();
	}

	@Bean(name = "SetResponseHeaderRouteFilter")
	public SetResponseHeaderRouteFilter setResponseHeaderRouteFilter() {
		return new SetResponseHeaderRouteFilter();
	}

	@Bean(name = "SetStatusRouteFilter")
	public SetStatusRouteFilter setStatusRouteFilter() {
		return new SetStatusRouteFilter();
	}

	@Configuration
	@ConditionalOnClass(Endpoint.class)
	protected static class GatewayActuatorConfiguration {

		@Bean
		public GatewayEndpoint gatewayEndpoint(List<GatewayFilter> filters) {
			return new GatewayEndpoint(filters);
		}
	}

}