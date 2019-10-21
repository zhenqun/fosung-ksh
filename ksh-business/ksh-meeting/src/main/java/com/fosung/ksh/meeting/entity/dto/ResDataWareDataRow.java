package com.fosung.ksh.meeting.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : yanxm
 * @date : 2019-04-10 16:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ResDataWareDataRow extends ResDataWareBase{


    private List<DataWareRow> data;

    @NoArgsConstructor
    @Data
    public static class DataWareRow {
        /**
         * itemName : 中国共产党莱州市委员会城港路街道工作委员会
         * type : ROW
         * itemValue : 123
         * properties : [{"itemName":"数量","type":"PROPERTY","itemValue":"120"}]
         */

        private String itemName;
        private String type;
        private String itemValue;
        private List<DataWareProperty> properties;

        @NoArgsConstructor
        @Data
        public static class DataWareProperty {
            /**
             * itemName : 数量
             * type : PROPERTY
             * itemValue : 120
             */

            private String itemName;
            private String type;
            private String itemValue;
        }
    }
}
