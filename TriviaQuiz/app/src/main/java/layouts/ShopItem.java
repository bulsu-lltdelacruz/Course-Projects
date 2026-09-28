package layouts;

public class ShopItem {
    public final String name;
    public final int price;
    public final int iconResId;
    public final String category; //icons,borders,themes
    public final int itemId;

    public ShopItem(int itemId, String name, int price, int iconResId, String category) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.iconResId = iconResId;
        this.category = category;
    }
}