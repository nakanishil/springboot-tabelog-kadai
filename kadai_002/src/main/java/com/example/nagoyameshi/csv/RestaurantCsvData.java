package com.example.nagoyameshi.csv;

import com.orangesignal.csv.annotation.CsvColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@com.orangesignal.csv.annotation.CsvEntity
/**
 * CSVデータ用
 * @author SAMURAI
 * @since 2024-12-25
 */
public class RestaurantCsvData {
    @CsvColumn(name = "飲食店番号")
    private Integer id;
    
    @CsvColumn(name = "名前")
    private String name;

    @CsvColumn(name = "郵便番号")
    private String postalCode;

    @CsvColumn(name = "住所")
    private String address;    
    
    @CsvColumn(name = "電話番号")
    private String phoneNumber;
}
