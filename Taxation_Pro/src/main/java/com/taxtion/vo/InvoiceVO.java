package com.taxtion.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int8;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceVO {
    private String creator;//发票创建人
    private String recipient;//收件人
    private BigInteger totalAmount;//总金额
    private String description;//描述
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;//创建日期
    private String auditInfo;//审计信息
    private BigInteger status;//状态0--pending 1--approval 2--payed

}
