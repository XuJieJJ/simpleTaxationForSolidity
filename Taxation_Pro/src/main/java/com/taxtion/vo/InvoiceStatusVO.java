package com.taxtion.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceStatusVO {

    public String InvoiceID;
    public BigInteger status;
}
