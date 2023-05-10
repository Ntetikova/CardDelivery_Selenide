package ru.netology;

import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }


    @BeforeEach
    void setUpTests() {

        open("http://localhost:9999/");
    }

    @Test
    public void sendFormHappyPath() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Супер Олег");
        $("[name='phone']").setValue("+79520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldHave(text("Встреча успешно забронирована на " + meetingDate), Duration.ofSeconds(15));

    }

    @Test
    public void sendFormWrongCity() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Гагарин");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Супер Олег");
        $("[name='phone']").setValue("+79520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='city']//span[@class='input__sub'][contains(text(), 'Доставка в выбранный город недоступна')]").should(visible);
    }

    @Test
    public void sendFormEmptyCity() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Супер Олег");
        $("[name='phone']").setValue("+79520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='city']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(visible);
    }

    @Test
    public void sendFormEmptyName() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Смоленск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[name='phone']").setValue("+79520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='name']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(visible);
    }

    @Test
    public void sendFormEmptyPhone() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Смоленск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Супер Олег");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='phone']//span[@class='input__sub'][contains(text(), 'Поле обязательно для заполнения')]").should(visible);
    }

    @Test
    public void sendFormWrongName() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Super Oleg");
        $("[name='phone']").setValue("+79520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='name']//span[@class='input__sub'][contains(text(), 'Имя и Фамилия указаные неверно')]").should(visible);
    }

    @Test
    public void sendFormWrongPhone() {
        String meetingDate = generateDate(3, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Супер Олег");
        $("[name='phone']").setValue("89520010203");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $x("//span[@data-test-id='phone']//span[@class='input__sub'][contains(text(), 'Телефон указан неверно')]").should(visible);
    }

    @Test
    public void sendFormAutoSelectCityAndMeetingDate() {


        $("[data-test-id='city'] input").sendKeys("Мо");
        ElementsCollection city = $$("div.menu-item");
        city.get(2).click();

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);

        String thisMonth = generateDate(0, "M");
        String meetingMonth = generateDate(32, "M");

        if (!thisMonth.equals(meetingMonth)) {
            $("div.calendar__arrow_direction_right[data-step='1']").click();
        }

        ElementsCollection days = $$("td.calendar__day");
       String meetingDate = generateDate(0, "d");
        days.findBy(exactText(meetingDate)).click();
        $("[data-test-id='date'] input").setValue(meetingDate);
        $("[data-test-id='name'] input").setValue("Череззаборногузадерищенский Петр");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $x("//div[contains(text(), 'Успешно!')]").should(visible, Duration.ofSeconds(15));
    }

}
