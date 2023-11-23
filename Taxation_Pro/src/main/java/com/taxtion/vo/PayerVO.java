package com.taxtion.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 纳税人实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayerVO {
    private  String name;
    private  String taxID;
    private  String contactInfo;
}
