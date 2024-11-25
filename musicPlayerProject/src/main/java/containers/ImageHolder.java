package containers;

public class ImageHolder {
    private String smallImage;
    private String mediumImage;
    private String largeImage;
    private String extraLargeImage;
    private String megaImage;

    public String getSmallImage() { return smallImage; }

    public String getMediumImage() { return mediumImage; }

    public String getLargeImage() { return largeImage; }

    public String getExtraLargeImage() { return extraLargeImage; }

    public String getMegaImage() { return megaImage; }

    public void setSmallImage(String smallImage) { this.smallImage = smallImage; }

    public void setMediumImage(String mediumImage) { this.mediumImage = mediumImage; }

    public void setLargeImage(String largeImage) { this.largeImage = largeImage; }

    public void setExtraLargeImage(String extraLargeImage) { this.extraLargeImage = extraLargeImage; }

    public void setMegaImage(String megaImage) { this.megaImage = megaImage; }
}