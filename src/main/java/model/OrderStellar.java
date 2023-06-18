package model;

import java.util.ArrayList;
/**
 * Содержит массив ингредиентов
 * можно получить нужный ингредиент или заменить
 */
public class OrderStellar {
    private ArrayList<String> ingredients;

    public OrderStellar(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
