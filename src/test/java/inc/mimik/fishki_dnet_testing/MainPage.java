package inc.mimik.fishki_dnet_testing;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
  public final SelenideElement HTML_XPATH = $x( "//html[contains(@class, 'html--adaptive')]" );
  public final SelenideElement LANG_SWITCH_XPATH = $x( "//li[contains(@class, 'header-settings__item header-settings__item_f')]" );
  public final SelenideElement RUS_LANG_SWITCH_XPATH = $x( "//a[contains(@href, '//fishki.net')]" );
  public final SelenideElement ENG_LANG_SWITCH_XPATH = $x( "//a[contains(@href, '//en.fishki.net')]" );
  public final SelenideElement SELECT_DATE_XPATH = $x( "//a[contains(@class, 'content__filter-link' ) and contains(@href, '#')]" );
  public final SelenideElement SELECT_DATE_TODAY_XPATH = $x( "//div[contains(@class, 'core border today')]" );
  public final SelenideElement LOGIN_BTN_XPATH = $x( "//a[contains(@class, 'auth-show spr icon icon__logout' ) and contains(@href, '/user/login/' )]" );
}
