package pl.adcom.teai_restapi.model;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

public class Car extends RepresentationModel {

    @NotBlank(message = "id may not be blank")
    private long id;
    @NotBlank(message = "mark may not be blank")
    private String mark;
    @NotBlank(message = "model may not be blank")
    private String model;
    @NotBlank(message = "color may not be blank")
    private Color color;

    public Car() {
    }

    public Car(long id, String mark, String model, Color color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", color=" + color +
                '}';
    }
}
