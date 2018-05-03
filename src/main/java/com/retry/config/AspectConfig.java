package com.retry.config;

import com.retry.aspect.RepeaterAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AspectConfig {
	
	@Bean
	public RepeaterAspect repeaterAspect(){ return new RepeaterAspect(); }
}