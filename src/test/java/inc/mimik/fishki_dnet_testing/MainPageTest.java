package inc.mimik.fishki_dnet_testing;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class MainPageTest {
  private final Logger LOGGER = LoggerFactory.getLogger( MainPageTest.class );
  private final MainPage MPAGE = new MainPage( );

  @BeforeAll
  public static void setUpAll( ) {
    Configuration.browserSize = "1280x800";
    SelenideLogger.addListener( "allure", new AllureSelenide( ) );
  }

  @BeforeEach
  public void setUp( ) {
    open( "https://fishki.net/" );
  }

  @Test
  public void testInitialLanguage( ) {
    MPAGE.HTML_XPATH.shouldBe( exist );
    MPAGE.HTML_XPATH.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testLanguageSwitchToEnglish( ) {
    MPAGE.LANG_SWITCH_XPATH.shouldBe( exist );
    MPAGE.LANG_SWITCH_XPATH.hover( );

    MPAGE.ENG_LANG_SWITCH_XPATH.shouldBe( visible );
    MPAGE.ENG_LANG_SWITCH_XPATH.click( );

    MPAGE.HTML_XPATH.shouldBe( exist );
    MPAGE.HTML_XPATH.shouldHave( attribute( "lang", "en" ) );
  }

  @Test
  public void testLanguageSwitchToRussian( ) {
    MPAGE.LANG_SWITCH_XPATH.shouldBe( exist );
    MPAGE.LANG_SWITCH_XPATH.hover( );

    MPAGE.RUS_LANG_SWITCH_XPATH.shouldBe( visible );
    MPAGE.RUS_LANG_SWITCH_XPATH.click( );

    MPAGE.HTML_XPATH.shouldBe( exist );
    MPAGE.HTML_XPATH.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testLanguageSwitchBack( ) {
    MPAGE.LANG_SWITCH_XPATH.shouldBe( exist );
    MPAGE.LANG_SWITCH_XPATH.hover( );

    MPAGE.ENG_LANG_SWITCH_XPATH.shouldBe( visible );
    MPAGE.ENG_LANG_SWITCH_XPATH.click( );

    MPAGE.LANG_SWITCH_XPATH.shouldBe( exist );
    MPAGE.LANG_SWITCH_XPATH.hover( );

    MPAGE.RUS_LANG_SWITCH_XPATH.shouldBe( visible );
    MPAGE.RUS_LANG_SWITCH_XPATH.click( );

    MPAGE.HTML_XPATH.shouldBe( exist );
    MPAGE.HTML_XPATH.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testInitialDate( ) {
    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.shouldHave( text( "за всё время" ) );
  }

  @Test
  public void testChangeToMonthBegin( ) throws ParseException {
    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.hover( );

    final SelenideElement month = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[1]" );
    final SelenideElement year = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[2]" );

    MPAGE.SELECT_DATE_TODAY_XPATH.shouldBe( exist );
    LOGGER.info( "Today is {} {} {}", MPAGE.SELECT_DATE_TODAY_XPATH.text(), month.text(), year.text() );

    final List<SelenideElement> dates = MPAGE.SELECT_DATE_TODAY_XPATH.parent().findAll( ".core" );
    SelenideElement begin = null;
    for ( SelenideElement d : dates ) {
      d.shouldBe( visible );
      // LOGGER.info( "available date: {}", d.text() );
      if ( d.text().equals( "1" ) ) {
        begin = d;
        break;
      }
    }

    if ( begin == null )
      throw new NullPointerException( "Can't find the beginning of the month" );

    LOGGER.info( "Setting date to {} {} {}", begin.text(), month.text(), year.text() );
    final String[] date = new String[]{ begin.text(), month.text(), year.text() };
    begin.shouldBe( visible );
    begin.hover( );
    begin.click( );

    final Date beginDate = (new SimpleDateFormat( "dd MMM yyyy", new Locale( "ru" ) )).parse( MessageFormatter.arrayFormat( "{} {} {}", date ).getMessage() );
    final String beginText = (new SimpleDateFormat( "dd.MM.yyyy", new Locale( "ru" ))).format( beginDate );
    LOGGER.info( "Set date to {}", beginText );

    final SelenideElement FIRST_DAY_SELECT_DATE_XPATH = $x( "//span[contains(@class, 'content__filter-label' )]" );
    FIRST_DAY_SELECT_DATE_XPATH.shouldBe( exist );
    FIRST_DAY_SELECT_DATE_XPATH.shouldHave( text( "за " + beginText ) );
  }
}
