package com.example.Pet.Clinic.Spring.Project.converter;

import org.javamoney.moneta.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MoneyConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money moneyFromEntity) {
        return moneyFromEntity.toString();
    }

    @Override
    public Money convertToEntityAttribute(String valueFromDatabase) {
        return Money.parse(valueFromDatabase);
    }

}
