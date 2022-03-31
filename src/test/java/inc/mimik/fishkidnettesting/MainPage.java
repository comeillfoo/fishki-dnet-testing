package inc.mimik.fishkidnettesting;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
  public SelenideElement html = $x( "//html[contains(@class, 'html--adaptive')]" );
  public SelenideElement languageSwitcher = $x( "//li[contains(@class, 'header-settings__item header-settings__item_f')]" );
  public SelenideElement russianLink = $x( "//a[contains(@href, '//fishki.net')]" );
  public SelenideElement englishLink = $x( "//a[contains(@href, '//en.fishki.net')]" );
  public SelenideElement changeData = $x( "//a[contains(@class, 'content__filter-link' ) and contains(@href, '#')]" );
  public SelenideElement today = $x( "//div[contains(@class, 'core border today')]" );
}
