public class BodyForCreateOrder {
    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    private String[] ingredients;

    public BodyForCreateOrder() {
    }

    public BodyForCreateOrder(String[] ingredients) {
        this.ingredients = ingredients;
    }
}

