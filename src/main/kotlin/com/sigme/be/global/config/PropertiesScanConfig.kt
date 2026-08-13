package com.sigme.be.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.sigme.be.global.properties"])
class PropertiesScanConfig {
}