import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class HappyPathTests {

    String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldMakeReservation() { //форма заполнена соответственно требованиям
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofMillis(15000));
        $(".notification__content").shouldHave(text(date));
    }

    @Test
    void shouldNotMakeReservationIfNoCity() { //поле города не заполнено
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotMakeReservationIfNoDate() { //поле даты не заполнено
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotMakeReservationIfNoName() { //поле имени не заполнено
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotMakeReservationIfNoPhone() { //поле ном телефона не заполнено
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotMakeReservationIfCheckboxNotChecked() { //чек бокс не выставлен
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $(".button").click();
        $(".input_invalid[data-test-id=agreement]").shouldBe(visible);
    }

    @Test
    void shouldNotMakeReservationIfIncorrectCity() { //поле города заполнено некорректно
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Moskow");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotMakeReservationIfIncorrectDate() { //поле даты заполнено некорректно
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("06.10.2021");
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotMakeReservationIfIncorrectName() { //поле имени заполнено некорректно
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Ivan");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotMakeReservationIncorrectPhone() { //поле ном телефона заполнено некорректно
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("89999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
}
