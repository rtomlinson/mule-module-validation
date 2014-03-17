/**
 * Mule Validation Module
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.modules.validation;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CodeValidator;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.FloatValidator;
import org.apache.commons.validator.routines.ISBNValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.apache.commons.validator.routines.PercentValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.ShortValidator;
import org.apache.commons.validator.routines.TimeValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.transport.NullPayload;
import org.mule.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A common issue when receiving data either electronically or from user input is verifying the integrity of the data.
 * This work is repetitive and becomes even more complicated when different sets of validation rules need to be applied
 * to the same set of data based on locale. Error messages may also vary by locale. This module addresses some of
 * these issues to speed development and maintenance of validation rules.
 *
 * @author MuleSoft, Inc.
 */
@Module(name = "validation", schemaVersion = "1.0")
public class ValidationModule {
    /**
     * If the specified <code>domain</code> does not parses as a valid domain name with a recognized top-level domain then
     * throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:is-domain}
     *
     * @param domain                   Domain name to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateDomain(String domain, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        DomainValidator validator = DomainValidator.getInstance();

        if (!validator.isValid(domain)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If if the specified <code>topLevelDomain</code> does not matches any IANA-defined top-level domain throw an exception.
     * Leading dots are ignored if present. The search is case-sensitive.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-top-level-domain}
     *
     * @param topLevelDomain           Domain name to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateTopLevelDomain(String topLevelDomain, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        DomainValidator validator = DomainValidator.getInstance();

        if (!validator.isValidTld(topLevelDomain)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If if the specified <code>countryCode</code> does not matches any IANA-defined top-level domain throw an exception.
     * Leading dots are ignored if present. The search is case-sensitive.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-top-level-domain-country}
     *
     * @param countryCode              Country code to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateTopLevelDomainCountry(String countryCode, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        DomainValidator validator = DomainValidator.getInstance();

        if (!validator.isValidCountryCodeTld(countryCode)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If if the specified <code>creditCardNumber</code> is not a valid credit card number throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-credit-card-number}
     *
     * @param creditCardNumber         Credit card number to validate
     * @param creditCardTypes          Credit card types to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateCreditCardNumber(String creditCardNumber, List<CreditCardType> creditCardTypes, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        CodeValidator[] validators = new CodeValidator[creditCardTypes.size()];
        int i = 0;
        for (CreditCardType type : creditCardTypes) {
            validators[i] = type.getCodeValidator();
            i++;
        }

        CreditCardValidator validator = new CreditCardValidator(validators);

        if (validator.validate(creditCardNumber) == null) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>emailAddress</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-email}
     *
     * @param emailAddress             Email address to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateEmail(String emailAddress, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        EmailValidator validator = EmailValidator.getInstance();

        if (!validator.isValid(emailAddress)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>ipAddress</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-ip-address}
     *
     * @param ipAddress                IP address to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateIpAddress(String ipAddress, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        InetAddressValidator validator = InetAddressValidator.getInstance();

        if (!validator.isValid(ipAddress)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>percentage</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-percentage}
     *
     * @param percentage               Percentage to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validatePercentage(String percentage, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        BigDecimalValidator validator = PercentValidator.getInstance();

        if (!validator.isValid(percentage)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>isbnCode</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-isbn10}
     *
     * @param isbnCode                 ISBN code to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor(name = "validate-isbn10")
    public void validateISBN10(String isbnCode, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        ISBNValidator validator = ISBNValidator.getInstance();

        if (!validator.isValidISBN10(isbnCode)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>isbnCode</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-isbn13}
     *
     * @param isbnCode                 ISBN code to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor(name = "validate-isbn13")
    public void validateISBN13(String isbnCode, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        ISBNValidator validator = ISBNValidator.getInstance();

        if (!validator.isValidISBN13(isbnCode)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>url</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-url}
     *
     * @param url                      URL to validate
     * @param allowTwoSlashes          Allow two slashes in the path component of the URL.
     * @param allowAllSchemes          Allows all validly formatted schemes to pass validation instead of supplying a set of valid schemes.
     * @param allowLocalURLs           Allow local URLs, such as http://localhost/ or http://machine/ .
     * @param noFragments              Enabling this options disallows any URL fragments.
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateUrl(String url,
                            @Optional @Default("false") boolean allowTwoSlashes,
                            @Optional @Default("false") boolean allowAllSchemes,
                            @Optional @Default("false") boolean allowLocalURLs,
                            @Optional @Default("false") boolean noFragments,
                            @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        long options = 0;

        if (allowAllSchemes) {
            options |= UrlValidator.ALLOW_ALL_SCHEMES;
        }
        if (allowTwoSlashes) {
            options |= UrlValidator.ALLOW_2_SLASHES;
        }
        if (allowLocalURLs) {
            options |= UrlValidator.ALLOW_LOCAL_URLS;
        }
        if (noFragments) {
            options |= UrlValidator.NO_FRAGMENTS;
        }

        UrlValidator validator = new UrlValidator(options);

        if (!validator.isValid(url)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>time</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-time}
     *
     * @param time                     Time to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateTime(String time, @Optional @Default("US") Locale locale, @Optional String pattern, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        TimeValidator validator = TimeValidator.getInstance();

        if (pattern != null) {
            if (!validator.isValid(time, locale.getJavaLocale())) {
                throw buildException(customExceptionClassName);
            }
        } else {
            if (!validator.isValid(time, pattern, locale.getJavaLocale())) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>date</code> is not a valid one throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-date}
     *
     * @param date                     Date to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateDate(String date, @Optional @Default("US") Locale locale, @Optional String pattern, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        DateValidator validator = DateValidator.getInstance();

        if (pattern != null) {
            if (!validator.isValid(date, locale.getJavaLocale())) {
                throw buildException(customExceptionClassName);
            }
        } else {
            if (!validator.isValid(date, pattern, locale.getJavaLocale())) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>value</code> does not match any of the regexs then throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-using-regex}
     *
     * @param value                    Value to match
     * @param regexs                   Set of regular expressions to test against
     * @param caseSensitive            when true matching is case sensitive, otherwise matching is case in-sensitive
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateUsingRegex(String value, List<String> regexs, @Optional @Default("false") boolean caseSensitive, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        RegexValidator validator = new RegexValidator(regexs.toArray(new String[]{}), caseSensitive);

        if (!validator.isValid(value)) {
            throw buildException(customExceptionClassName);
        }
    }

    /**
     * If the specified <code>value</code> is not a valid {@link Long} throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-long}
     *
     * @param value                    Value to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param minValue                 The minimum value
     * @param maxValue                 The maximum value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateLong(String value, @Optional @Default("US") Locale locale, @Optional String pattern,
                             @Optional Long minValue, @Optional Long maxValue,
                             @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        LongValidator validator = LongValidator.getInstance();

        Long newValue = null;
        if (pattern != null) {
            newValue = validator.validate(value, pattern, locale.getJavaLocale());
        } else {
            newValue = validator.validate(value, locale.getJavaLocale());
        }

        if (newValue == null) {
            throw buildException(customExceptionClassName);
        }
        if (minValue != null) {
            if (!validator.minValue(newValue, minValue)) {
                throw buildException(customExceptionClassName);
            }
        }
        if (maxValue != null) {
            if (!validator.maxValue(newValue, maxValue)) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>value</code> is not a valid {@link Integer} throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-integer}
     *
     * @param value                    Value to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param minValue                 The minimum value
     * @param maxValue                 The maximum value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateInteger(String value, @Optional @Default("US") Locale locale, @Optional String pattern,
                                @Optional Integer minValue, @Optional Integer maxValue,
                                @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        IntegerValidator validator = IntegerValidator.getInstance();

        Integer newValue = null;
        if (pattern != null) {
            newValue = validator.validate(value, pattern, locale.getJavaLocale());
        } else {
            newValue = validator.validate(value, locale.getJavaLocale());
        }

        if (newValue == null) {
            throw buildException(customExceptionClassName);
        }
        if (minValue != null) {
            if (!validator.minValue(newValue, minValue)) {
                throw buildException(customExceptionClassName);
            }
        }
        if (maxValue != null) {
            if (!validator.maxValue(newValue, maxValue)) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>value</code> is not a valid {@link Float} throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-float}
     *
     * @param value                    Value to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param minValue                 The minimum value
     * @param maxValue                 The maximum value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateFloat(String value, @Optional @Default("US") Locale locale, @Optional String pattern,
                              @Optional Float minValue, @Optional Float maxValue,
                              @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        FloatValidator validator = FloatValidator.getInstance();

        Float newValue = null;
        if (pattern != null) {
            newValue = validator.validate(value, pattern, locale.getJavaLocale());
        } else {
            newValue = validator.validate(value, locale.getJavaLocale());
        }

        if (newValue == null) {
            throw buildException(customExceptionClassName);
        }
        if (minValue != null) {
            if (!validator.minValue(newValue, minValue)) {
                throw buildException(customExceptionClassName);
            }
        }
        if (maxValue != null) {
            if (!validator.maxValue(newValue, maxValue)) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>value</code> is not a valid {@link Double} throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-double}
     *
     * @param value                    Value to validate
     * @param locale                   The locale to use for the format
     * @param pattern                  The pattern used to format the value
     * @param minValue                 The minimum value
     * @param maxValue                 The maximum value
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateDouble(String value, @Optional @Default("US") Locale locale, @Optional String pattern,
                               @Optional Double minValue, @Optional Double maxValue,
                               @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        DoubleValidator validator = DoubleValidator.getInstance();

        Double newValue = null;
        if (pattern != null) {
            newValue = validator.validate(value, pattern, locale.getJavaLocale());
        } else {
            newValue = validator.validate(value, locale.getJavaLocale());
        }

        if (newValue == null) {
            throw buildException(customExceptionClassName);
        }
        if (minValue != null) {
            if (!validator.minValue(newValue, minValue)) {
                throw buildException(customExceptionClassName);
            }
        }
        if (maxValue != null) {
            if (!validator.maxValue(newValue, maxValue)) {
                throw buildException(customExceptionClassName);
            }
        }
    }


    /**
     * If the specified <code>object</code> is empty or null throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-not-empty}
     *
     * @param object                   Object to validate
     * @param customExceptionClassName Class name of the exception to throw
     * @throws Exception if not valid
     */
    @Processor
    public void validateNotEmpty(@Optional @Default("#[payload]") Object object, @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        if (object == null || object instanceof NullPayload) {
            throw buildException(customExceptionClassName);
        }

        if (isCollection(object.getClass())) {
            if (((Collection) object).size() == 0) {
                throw buildException(customExceptionClassName);
            }
        }

        if (isMap(object.getClass())) {
            if (((Map) object).size() == 0) {
                throw buildException(customExceptionClassName);
            }
        }

        if (object instanceof String) {
            if (((String) object).length() == 0) {
                throw buildException(customExceptionClassName);
            }
        }
    }

    /**
     * If the specified <code>input</code> is not within the valid limits throw an exception.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-validation.xml.sample validation:validate-length}
     *
     * @param input                     String to validate
     * @param minValue                  the minimum value
     * @param maxValue                  the maximum value
     * @param customExceptionClassName  Class name of the exception to throw
     * @throws Exception Exception if not valid
     */
    @Processor
    public void validateLength(String input,
                               @Optional @Default("0") Integer minValue, Integer maxValue,
                               @Optional @Default("org.mule.modules.validation.InvalidException") String customExceptionClassName) throws Exception {
        if(input == null || maxValue == null) {
            throw buildException(customExceptionClassName);
        }

        int inputLength = input.length();
        if(inputLength < minValue || inputLength > maxValue) {
            throw buildException(customExceptionClassName);
        }
    }

    private Exception buildException(String customExceptionClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> exceptionClass = Class.forName(customExceptionClassName);

        if (!isException(exceptionClass)) {
            throw new IllegalArgumentException("The class name must be that of a Throwable class");
        }

        return (Exception) exceptionClass.newInstance();
    }

    /**
     * Checks whether the specified class parameter is an instance of {@link Exception }
     *
     * @param clazz <code>Class</code> to check.
     * @return
     */
    private boolean isException(Class clazz) {
        List<Class> classes = new ArrayList<Class>();
        computeClassHierarchy(clazz, classes);
        return classes.contains(Exception.class);
    }

    /**
     * Checks whether the specified class parameter is an instance of {@link Collection }
     *
     * @param clazz <code>Class</code> to check.
     * @return
     */
    private boolean isCollection(Class clazz) {
        List<Class> classes = new ArrayList<Class>();
        computeClassHierarchy(clazz, classes);
        return classes.contains(Collection.class);
    }

    /**
     * Checks whether the specified class parameter is an instance of {@link Collection }
     *
     * @param clazz <code>Class</code> to check.
     * @return
     */
    private boolean isMap(Class clazz) {
        List<Class> classes = new ArrayList<Class>();
        computeClassHierarchy(clazz, classes);
        return classes.contains(Map.class);
    }

    /**
     * Get all superclasses and interfaces recursively.
     *
     * @param classes List of classes to which to add all found super classes and interfaces.
     * @param clazz   The class to start the search with.
     */
    private void computeClassHierarchy(Class clazz, List classes) {
        for (Class current = clazz; (current != null); current = current.getSuperclass()) {
            if (classes.contains(current)) {
                return;
            }
            classes.add(current);
            for (Class currentInterface : current.getInterfaces()) {
                computeClassHierarchy(currentInterface, classes);
            }
        }
    }
}
