package inc.mimik.fishki_dnet_testing;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

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
    clearBrowserCookies();
    clearBrowserLocalStorage();
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

  private SelenideElement getCurrentMonth( ) {
    return $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[1]" );
  }

  private SelenideElement getCurrentYear( ) {
    return $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[2]" );
  }

  private String changeToMonthDay( int day ) throws ParseException {
    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.hover( );

    final SelenideElement month = getCurrentMonth( );
    final SelenideElement year = getCurrentYear( );

    SelenideElement maybeToday = null;
    if ( MPAGE.SELECT_DATE_TODAY_XPATH.is( exist ) ) {
      maybeToday = MPAGE.SELECT_DATE_TODAY_XPATH;
      LOGGER.info( "Today is {} {} {}", MPAGE.SELECT_DATE_TODAY_XPATH.text(), month.text(), year.text() );
    } else maybeToday = $x( "//div[contains(@class, 'gldp-default')]//div[contains(@class, 'core')]" );

    final List<SelenideElement> dates = maybeToday.parent().findAll( ".core" );
    SelenideElement begin = null;
    for ( SelenideElement d : dates ) {
      d.shouldBe( visible );
      // LOGGER.info( "available date: {}", d.text() );
      if ( d.text().equals( String.valueOf( day ) ) ) {
        begin = d;
        break;
      }
    }

    if ( begin == null )
      throw new NullPointerException( String.format( "Can't find the day [ %d ] of the month", day ) );

    LOGGER.info( "Setting date to {} {} {}", begin.text(), month.text(), year.text() );
    final String[] date = new String[]{ begin.text(), month.text(), year.text() };
    begin.shouldBe( visible );
    begin.hover( );
    begin.click( );

    final Date beginDate = (new SimpleDateFormat( "dd MMM yyyy", new Locale( "ru" ) )).parse( MessageFormatter.arrayFormat( "{} {} {}", date ).getMessage() );
    final String beginText = (new SimpleDateFormat( "dd.MM.yyyy", new Locale( "ru" ))).format( beginDate );
    LOGGER.info( "Set date to {}", beginText );
    return beginText;
  }

  @ParameterizedTest
  @CsvSource( { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22" } )
  public void testDateChangeToPreviousMonthDay( int day ) throws ParseException {
    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.hover( );

    final SelenideElement prevArrow = $x( "//div[contains(@class, 'core border monyear')]//a[contains(@class, 'prev-arrow')]" );
    prevArrow.shouldBe( visible );
    prevArrow.click();

    String beginText = changeToMonthDay( day );
    final SelenideElement FIRST_DAY_SELECT_DATE_XPATH = $x( "//span[contains(@class, 'content__filter-label' )]" );
    FIRST_DAY_SELECT_DATE_XPATH.shouldBe( exist );
    FIRST_DAY_SELECT_DATE_XPATH.shouldHave( text( "за " + beginText ) );
  }

  private static final String[] MONTHS = {
      "январь", "февраль", "март",
      "апрель", "май", "июнь",
      "июль", "август", "сентябрь",
      "октябрь", "ноябрь", "декабрь"
  };

  @ParameterizedTest
  @CsvSource( { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" } )
  public void testMonthChangeToFirstOfNthMonthOfPreviousYear( int monthValue ) throws ParseException {
    LOGGER.info( "Set month to {}", MONTHS[ monthValue ] );

    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.hover( );

    final SelenideElement prevArrow = $x( "//div[contains(@class, 'core border monyear')]//a[contains(@class, 'prev-arrow')]" );
    final SelenideElement nextArrow = $x( "//div[contains(@class, 'core border monyear')]//a[contains(@class, 'next-arrow')]" );
    prevArrow.shouldBe( visible );
    prevArrow.click();

    nextArrow.shouldBe( visible );
    nextArrow.click();

    // select previous year
    final SelenideElement year = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[2]" );
    year.shouldBe( visible );
    int nYear = Integer.parseInt( year.text() );
    nYear = nYear < 2009? 2009 : nYear;
    LOGGER.info( "Valid year is {}", nYear );
    year.click( );

    final SelenideElement yearSelector = $x("(//div[contains(@class, 'core border monyear title')]//div//select)[2]" );
    yearSelector.shouldBe( visible );
    yearSelector.selectOptionByValue( String.valueOf( nYear - 1 ) );

    final SelenideElement month = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[1]" );

    month.shouldBe( visible );
    final String monthName = month.text();
    month.click();

    final SelenideElement monthSelector = $x( "(//div[contains(@class, 'core border monyear title')]//div//select)[1]" );
    final boolean isCurrent = MONTHS[ monthValue ].equalsIgnoreCase( monthName );
    LOGGER.info( "Is testing current month ( {} ): {}", monthName, isCurrent );
    if ( isCurrent ) {
      monthSelector.shouldBe( visible );
      monthSelector.selectOptionByValue( String.valueOf( ( 12 + monthValue - 1 ) % 12 ) ); // to select previous Month

      month.shouldBe( visible );
      month.click( );
    }

    monthSelector.selectOptionByValue( String.valueOf( monthValue ) ); // to select Nth Month

    month.shouldBe( visible );
    month.shouldHave( text( MONTHS[ monthValue ] ));

    final String beginText = changeToMonthDay( 1 );

    final SelenideElement FIRST_DAY_SELECT_DATE_XPATH = $x( "//span[contains(@class, 'content__filter-label' )]" );
    FIRST_DAY_SELECT_DATE_XPATH.shouldBe( exist );
    FIRST_DAY_SELECT_DATE_XPATH.shouldHave( text( "за " + beginText ) );
  }

  @ParameterizedTest
  @CsvSource( {
      "2009", "2010", "2011", "2012", "2013", "2014",
      "2015", "2016", "2017", "2018", "2019", "2020", "2021" } )
  public void testYearChangeToFirstOfCurrentMonth( int yearValue ) throws ParseException {
    LOGGER.info( "Set year to {}", yearValue );

    MPAGE.SELECT_DATE_XPATH.shouldBe( exist );
    MPAGE.SELECT_DATE_XPATH.hover( );

    final SelenideElement prevArrow = $x( "//div[contains(@class, 'core border monyear')]//a[contains(@class, 'prev-arrow')]" );
    prevArrow.shouldBe( visible );
    prevArrow.click();

    final SelenideElement nextArrow = $x( "//div[contains(@class, 'core border monyear')]//a[contains(@class, 'next-arrow')]" );
    nextArrow.shouldBe( visible );
    nextArrow.click();

    final SelenideElement year = $x( "(//div[contains(@class, 'core border monyear title')]//div//span)[2]" );
    year.shouldBe( visible );
    LOGGER.info( "Current year is {}", year.text() );
    year.click( );

    final SelenideElement yearSelector = $x("(//div[contains(@class, 'core border monyear title')]//div//select)[2]" );
    yearSelector.shouldBe( visible );
    yearSelector.selectOptionByValue( String.valueOf( yearValue ) );

    year.shouldBe( visible );

    final String beginText = changeToMonthDay( 1 );

    final SelenideElement FIRST_DAY_SELECT_DATE_XPATH = $x( "//span[contains(@class, 'content__filter-label' )]" );
    FIRST_DAY_SELECT_DATE_XPATH.shouldBe( exist );
    FIRST_DAY_SELECT_DATE_XPATH.shouldHave( text( "за " + beginText ) );
  }

  private void openRegistrationWindow() {
    // open login window
    MPAGE.LOGIN_BTN_XPATH.shouldBe( visible );
    MPAGE.LOGIN_BTN_XPATH.click();

    final SelenideElement AUTH_WINDOW = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[1]" );
    AUTH_WINDOW.shouldBe( visible );

    // open registration window
    final SelenideElement CREATE_ACCOUNT_BTN = $x( "//a[contains(@class, 'simplemodal-close login-switcher') and contains(@href, '#')]" );
    CREATE_ACCOUNT_BTN.shouldBe( exist );

    CREATE_ACCOUNT_BTN.click( );
  }

  @Test
  public void testRegistrationButton( ) {
    openRegistrationWindow();

    // check if registration window appears
    final SelenideElement REGISTRATION_WINDOW = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]" );
    REGISTRATION_WINDOW.shouldBe( visible );
  }

  @Test
  public void testReturnBackButtonOnRegistration( ) {
    openRegistrationWindow();

    // click on return back button
    final SelenideElement RETURN_BACK_BTN = $x( "//form[contains(@class, 'form-auth form-generic') and contains( @action, '/user/login/register' )]/a[contains( @class, 'login-switcher' ) and contains( @href, '#' )]" );
    RETURN_BACK_BTN.shouldBe( visible );

    RETURN_BACK_BTN.click();

    // check if authorization window appeared
    final SelenideElement AUTH_WINDOW = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[1]" );
    AUTH_WINDOW.shouldBe( visible );
  }

  @Test
  public void testDisagreeWithUserAgreement( ) {
    openRegistrationWindow();

    final SelenideElement userAgreementContainer = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]" ).$( "div.comments-form__checkboxes" );
    userAgreementContainer.shouldBe( visible );
    final SelenideElement userAgreementCheckBox = userAgreementContainer.$( "label" );
    userAgreementCheckBox.shouldBe( visible );

    userAgreementCheckBox.click( ClickOptions.usingJavaScript().offset( 0, 0 ) );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = userAgreementCheckBox.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Необходимо принять пользовательское соглашение" ) );
  }

  @Test
  public void testDisagreeWithPersonalDataProcessing( ) {
    openRegistrationWindow();

    final SelenideElement personalDataContainer = $x( "((//div[contains(@class, 'modal modal-auth' )]/div)[3]//div[contains(@class, 'comments-form__checkboxes')])[2]" );
    personalDataContainer.shouldBe( visible );
    final SelenideElement personalDataCheckBox = personalDataContainer.$( "label" );
    personalDataCheckBox.shouldBe( visible );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = personalDataCheckBox.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Необходимо согласиться с передачей и обработкой персональных данных" ) );
  }

  @ParameterizedTest
  @CsvSource( {
      "''", // empty
      "' '", // less than 2 and spaces
      "'                 '", // only spaces
      "мой некорректный логин", // cyrillic
      "theLongestLoginInTheWorldOfLoginsTHAT___shouldBreakTheLoginTest", // too long login
      "_", // less than 2 but correct symbol set
      "!@#)&*^%$#*%^(Q*&$)@#_+$@*%_", // incorrect symbols
  } )
  public void testIncorrectLoginInput( String incorrectLogin ) {
    openRegistrationWindow();

    final SelenideElement loginInput = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//input[contains(@name, 'login') and contains(@type, 'text')]" );
    loginInput.shouldBe( visible );
    /*
     * Логин должен быть длиной не менее двух и не более 32-х символов
     * Допускаются только латинские буквы, цифры и символ подчёркивания.
     */
    LOGGER.info( "trying incorrect login: {}", incorrectLogin );
    loginInput.setValue( incorrectLogin );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = loginInput.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Вы ввели некорректный логин. Он должен быть длиной не менее двух и не более 32-х символов. Допускаются только латинские буквы, цифры и символ подчёркивания." ) );
  }

  @ParameterizedTest
  @CsvSource( {
      "admin",
      "cinnamon"
  } )
  public void testTakenLogins( String takenLogin ) {
    openRegistrationWindow();

    final SelenideElement loginInput = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//input[contains(@name, 'login') and contains(@type, 'text')]" );
    loginInput.shouldBe( visible );

    LOGGER.info( "trying taken login: {}", takenLogin );
    loginInput.setValue( takenLogin );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = loginInput.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Выбранный логин уже используется: выберите другой." ) );
  }

  @ParameterizedTest
  @CsvSource( {
      "''",
      "' '",
      "'  '",
      "'   '",
      "1",
      "12",
      "123",
      "a a",
  } )
  public void testIncorrectPassword( String incorrectPassword ) {
    openRegistrationWindow();

    final SelenideElement passInput = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//input[contains(@name, 'password') and contains(@type, 'password')]" );
    passInput.shouldBe( visible );

    LOGGER.info( "trying incorrect password: {}", incorrectPassword );
    passInput.setValue( incorrectPassword );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = passInput.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Вы ввели некорректный пароль. Он может содержать любые символы, но его длина должна быть не менее четырех и не более 32-х символов." ) );
  }

  @ParameterizedTest
  @CsvSource( {
      "'123', '456'", // totally not matching passwords
      "'aaab', 'aaa'", // common prefix
      "'aab', 'aa'",
      "'ab', 'a'",
      "'ba', 'a'", // common suffix
      "'baa', 'aa'",
      "'baaa', 'aaa'",
      "'baaab', 'aaa'", // substring
      "'bbaabb', 'aa'"
  } )
  public void testNotMatchingPasswords( String password, String anotherPassword ) {
    openRegistrationWindow();

    final SelenideElement passInput = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//input[contains(@name, 'password') and contains(@type, 'password')]" );
    passInput.shouldBe( visible );

    LOGGER.info( "entering password: {}", password );
    passInput.setValue( password );

    final SelenideElement rePassInput = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//input[contains(@name, 'repassword') and contains(@type, 'password')]" );
    rePassInput.shouldBe( visible );

    LOGGER.info( "repeating password: {}", anotherPassword );
    rePassInput.setValue( anotherPassword );

    final SelenideElement registrationButton = $x( "(//div[contains(@class, 'modal modal-auth' )]/div)[3]//button[contains(@class, 'btn-green btn-green--bdtl')]" );
    registrationButton.shouldBe( visible );

    registrationButton.click( );

    final SelenideElement errorText = rePassInput.parent().$x( ".//div[contains(@class, 'error')]" );
    errorText.shouldBe( visible );
    errorText.shouldHave( text( "Вы ввели разные пароли в поля «Пароль» и «Подтверждение пароля»." ) );
  }
}
