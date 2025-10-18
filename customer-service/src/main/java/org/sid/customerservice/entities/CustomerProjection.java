package org.sid.customerservice.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(types = Customer.class, name = "customerProjection")
public interface CustomerProjection {
    public String getid();
    public String getname();
    public String getemail();
}