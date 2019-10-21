package com.fosung.ksh.punsh.vo;


import com.fosung.ksh.punsh.entity.AdministrativeDivision;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @date 2019-4-3 14:32
 */
@Data
@ToString
public class Administration extends AdministrativeDivision {


    List<Administration> children;
}
