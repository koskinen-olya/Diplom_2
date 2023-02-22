public class Order {
    private String[] ingredients;

    //Getter and Setter for ingredients
    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }
}

