package inc.mimik.fishkidnettesting;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.HoverOptions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LoggerFactoryBinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
  private final Logger LOGGER = LoggerFactory.getLogger( MainPageTest.class );
  private final MainPage mainPage = new MainPage( );

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
    mainPage.html.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testLanguageSwitchToEnglish( ) {
    mainPage.languageSwitcher.hover( );
    mainPage.englishLink.click( );
    mainPage.html.shouldHave( attribute( "lang", "en" ) );
  }

  @Test
  public void testLanguageSwitchToRussian( ) {
    mainPage.languageSwitcher.hover( );
    mainPage.russianLink.click( );
    mainPage.html.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testLanguageSwitchBack( ) {
    mainPage.languageSwitcher.hover( );
    mainPage.englishLink.click( );
    mainPage.languageSwitcher.hover( );
    mainPage.russianLink.click( );
    mainPage.html.shouldHave( attribute( "lang", "ru" ) );
  }

  @Test
  public void testInitialDate( ) {
    mainPage.changeData.shouldHave( text( "за всё время" ) );
  }

  @Test
  public void testChangeToMonthBegin( ) throws ParseException {
    mainPage.changeData.hover( );
    final SelenideElement month = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[1]" );
    final SelenideElement year = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[2]" );
    LOGGER.info( "Today is {} {} {}", mainPage.today.text(), month.text(), year.text() );
    final List<SelenideElement> dates = mainPage.today.parent().findAll( "div.core.border" );
    SelenideElement begin = null;
    for ( SelenideElement d : dates ) {
      if ( d.text().equals( "1" ) )
        begin = d;
    }
    LOGGER.info( "Setting date to {} {} {}", begin.text(), month.text(), year.text() );
    begin.click( );
    final Date beginDate = (new SimpleDateFormat( "dd MMM yyyy", new Locale( "ru" ) )).parse( MessageFormatter.arrayFormat( "{} {} {}", new String[]{ begin.text(), month.text(), year.text() } ).getMessage() );
    final String beginText = (new SimpleDateFormat( "dd.MM.yyyy", new Locale( "ru" ))).format( beginDate );
    LOGGER.info( "Set date to {}", beginText );
//    final SelenideElement container = $x( "//div[contains(@class, 'content__filter-wrap datepicker datepicker--day')]");
//    container.$x( "//span[contains( @class, 'content__filter-label')]" ).shouldHave( text( String.format( "За %s", beginText ) ) );
  }
}
