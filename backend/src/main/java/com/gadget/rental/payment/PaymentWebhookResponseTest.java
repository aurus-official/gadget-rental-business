package com.gadget.rental.payment;

import java.util.List;
import java.util.Map;

public class PaymentWebhookResponseTest {

    private String id;
    private List<Item> items;
    private String requestReferenceNumber;
    private String receiptNumber;
    private String createdAt;
    private String updatedAt;
    private String paymentScheme;
    private boolean expressCheckout;
    private String refundedAmount;
    private boolean canPayPal;
    private String expiredAt;
    private String status;
    private String paymentStatus;
    private PaymentDetails paymentDetails;
    private String paymentAt;
    private boolean is3ds;
    private Buyer buyer;
    private Merchant merchant;
    private TotalAmount totalAmount;
    private RedirectUrl redirectUrl;
    private String transactionReferenceNumber;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getRequestReferenceNumber() {
        return requestReferenceNumber;
    }

    public void setRequestReferenceNumber(String requestReferenceNumber) {
        this.requestReferenceNumber = requestReferenceNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(String paymentScheme) {
        this.paymentScheme = paymentScheme;
    }

    public boolean isExpressCheckout() {
        return expressCheckout;
    }

    public void setExpressCheckout(boolean expressCheckout) {
        this.expressCheckout = expressCheckout;
    }

    public String getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(String refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public boolean isCanPayPal() {
        return canPayPal;
    }

    public void setCanPayPal(boolean canPayPal) {
        this.canPayPal = canPayPal;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentAt() {
        return paymentAt;
    }

    public void setPaymentAt(String paymentAt) {
        this.paymentAt = paymentAt;
    }

    public boolean is3ds() {
        return is3ds;
    }

    public void set3ds(boolean is3ds) {
        this.is3ds = is3ds;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public TotalAmount getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(TotalAmount totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RedirectUrl getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(RedirectUrl redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    // Nested static classes for items, payment details, buyer, merchant, etc.

    public static class Item {
        private String name;
        private String code;
        private String description;
        private String quantity;
        private Amount amount;
        private Amount totalAmount;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Amount getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Amount totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    public static class Amount {
        private double value;

        // Getters and Setters
        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    public static class PaymentDetails {
        private Map<String, Response> responses;
        private String createdAt;

        // Getters and Setters
        public Map<String, Response> getResponses() {
            return responses;
        }

        public void setResponses(Map<String, Response> responses) {
            this.responses = responses;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public static class Response {
            private String paymentTransactionReferenceNo;
            private String status;
            private Receipt receipt;
            private Payer payer;
            private Amount amount;

            // Getters and Setters
            public String getPaymentTransactionReferenceNo() {
                return paymentTransactionReferenceNo;
            }

            public void setPaymentTransactionReferenceNo(String paymentTransactionReferenceNo) {
                this.paymentTransactionReferenceNo = paymentTransactionReferenceNo;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Receipt getReceipt() {
                return receipt;
            }

            public void setReceipt(Receipt receipt) {
                this.receipt = receipt;
            }

            public Payer getPayer() {
                return payer;
            }

            public void setPayer(Payer payer) {
                this.payer = payer;
            }

            public Amount getAmount() {
                return amount;
            }

            public void setAmount(Amount amount) {
                this.amount = amount;
            }

            public static class Receipt {
                private String transactionId;
                private String receiptNo;
                private String approvalCode;

                // Getters and Setters
                public String getTransactionId() {
                    return transactionId;
                }

                public void setTransactionId(String transactionId) {
                    this.transactionId = transactionId;
                }

                public String getReceiptNo() {
                    return receiptNo;
                }

                public void setReceiptNo(String receiptNo) {
                    this.receiptNo = receiptNo;
                }

                public String getApprovalCode() {
                    return approvalCode;
                }

                public void setApprovalCode(String approvalCode) {
                    this.approvalCode = approvalCode;
                }
            }

            public static class Payer {
                private FundingInstrument fundingInstrument;

                // Getters and Setters
                public FundingInstrument getFundingInstrument() {
                    return fundingInstrument;
                }

                public void setFundingInstrument(FundingInstrument fundingInstrument) {
                    this.fundingInstrument = fundingInstrument;
                }

                public static class FundingInstrument {
                    private Card card;

                    // Getters and Setters
                    public Card getCard() {
                        return card;
                    }

                    public void setCard(Card card) {
                        this.card = card;
                    }

                    public static class Card {
                        private String cardNumber;
                        private int expiryMonth;
                        private String expiryYear;

                        // Getters and Setters
                        public String getCardNumber() {
                            return cardNumber;
                        }

                        public void setCardNumber(String cardNumber) {
                            this.cardNumber = cardNumber;
                        }

                        public int getExpiryMonth() {
                            return expiryMonth;
                        }

                        public void setExpiryMonth(int expiryMonth) {
                            this.expiryMonth = expiryMonth;
                        }

                        public String getExpiryYear() {
                            return expiryYear;
                        }

                        public void setExpiryYear(String expiryYear) {
                            this.expiryYear = expiryYear;
                        }
                    }
                }
            }
        }
    }

    public static class Buyer {
        private Contact contact;
        private String firstName;
        private String lastName;
        private Address billingAddress;
        private Address shippingAddress;

        // Getters and Setters
        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Address getBillingAddress() {
            return billingAddress;
        }

        public void setBillingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
        }

        public Address getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public static class Contact {
            private String phone;
            private String email;

            // Getters and Setters
            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

        public static class Address {
            private String line1;
            private String line2;
            private String city;
            private String state;
            private String zipCode;
            private String countryCode;

            // Getters and Setters
            public String getLine1() {
                return line1;
            }

            public void setLine1(String line1) {
                this.line1 = line1;
            }

            public String getLine2() {
                return line2;
            }

            public void setLine2(String line2) {
                this.line2 = line2;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getZipCode() {
                return zipCode;
            }

            public void setZipCode(String zipCode) {
                this.zipCode = zipCode;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }
        }
    }

    public static class Merchant {
        private String currency;
        private String email;
        private String locale;
        private String homepageUrl;
        private boolean isEmailToMerchantEnabled;
        private boolean isEmailToBuyerEnabled;
        private boolean isPaymentFacilitator;
        private boolean isPageCustomized;
        private List<String> supportedSchemes;
        private boolean canPayPal;
        private String payPalEmail;
        private String payPalWebExperienceId;
        private boolean expressCheckout;
        private String name;

        // Getters and Setters
        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getHomepageUrl() {
            return homepageUrl;
        }

        public void setHomepageUrl(String homepageUrl) {
            this.homepageUrl = homepageUrl;
        }

        public boolean isEmailToMerchantEnabled() {
            return isEmailToMerchantEnabled;
        }

        public void setEmailToMerchantEnabled(boolean isEmailToMerchantEnabled) {
            this.isEmailToMerchantEnabled = isEmailToMerchantEnabled;
        }

        public boolean isEmailToBuyerEnabled() {
            return isEmailToBuyerEnabled;
        }

        public void setEmailToBuyerEnabled(boolean isEmailToBuyerEnabled) {
            this.isEmailToBuyerEnabled = isEmailToBuyerEnabled;
        }

        public boolean isPaymentFacilitator() {
            return isPaymentFacilitator;
        }

        public void setPaymentFacilitator(boolean isPaymentFacilitator) {
            this.isPaymentFacilitator = isPaymentFacilitator;
        }

        public boolean isPageCustomized() {
            return isPageCustomized;
        }

        public void setPageCustomized(boolean isPageCustomized) {
            this.isPageCustomized = isPageCustomized;
        }

        public List<String> getSupportedSchemes() {
            return supportedSchemes;
        }

        public void setSupportedSchemes(List<String> supportedSchemes) {
            this.supportedSchemes = supportedSchemes;
        }

        public boolean isCanPayPal() {
            return canPayPal;
        }

        public void setCanPayPal(boolean canPayPal) {
            this.canPayPal = canPayPal;
        }

        public String getPayPalEmail() {
            return payPalEmail;
        }

        public void setPayPalEmail(String payPalEmail) {
            this.payPalEmail = payPalEmail;
        }

        public String getPayPalWebExperienceId() {
            return payPalWebExperienceId;
        }

        public void setPayPalWebExperienceId(String payPalWebExperienceId) {
            this.payPalWebExperienceId = payPalWebExperienceId;
        }

        public boolean isExpressCheckout() {
            return expressCheckout;
        }

        public void setExpressCheckout(boolean expressCheckout) {
            this.expressCheckout = expressCheckout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TotalAmount {
        private String value;
        private String currency;
        private AmountDetails details;

        // Getters and Setters
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public AmountDetails getDetails() {
            return details;
        }

        public void setDetails(AmountDetails details) {
            this.details = details;
        }

        public static class AmountDetails {
            private String discount;
            private String serviceCharge;
            private String shippingFee;
            private String tax;
            private String subtotal;

            // Getters and Setters
            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getServiceCharge() {
                return serviceCharge;
            }

            public void setServiceCharge(String serviceCharge) {
                this.serviceCharge = serviceCharge;
            }

            public String getShippingFee() {
                return shippingFee;
            }

            public void setShippingFee(String shippingFee) {
                this.shippingFee = shippingFee;
            }

            public String getTax() {
                return tax;
            }

            public void setTax(String tax) {
                this.tax = tax;
            }

            public String getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(String subtotal) {
                this.subtotal = subtotal;
            }
        }
    }

    public static class RedirectUrl {
        private String success;
        private String failure;
        private String cancel;

        // Getters and Setters
        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getFailure() {
            return failure;
        }

        public void setFailure(String failure) {
            this.failure = failure;
        }

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }
    }

    public boolean isIs3ds() {
        return is3ds;
    }

    public void setIs3ds(boolean is3ds) {
        this.is3ds = is3ds;
    }

    @Override
    public String toString() {
        return "PaymentWebhookResponseTest [id=" + id + ", items=" + items + ", requestReferenceNumber="
                + requestReferenceNumber + ", receiptNumber=" + receiptNumber + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", paymentScheme=" + paymentScheme + ", expressCheckout="
                + expressCheckout + ", refundedAmount=" + refundedAmount + ", canPayPal=" + canPayPal + ", expiredAt="
                + expiredAt + ", status=" + status + ", paymentStatus=" + paymentStatus + ", paymentDetails="
                + paymentDetails + ", paymentAt=" + paymentAt + ", is3ds=" + is3ds + ", buyer=" + buyer + ", merchant="
                + merchant + ", totalAmount=" + totalAmount + ", redirectUrl=" + redirectUrl
                + ", transactionReferenceNumber=" + transactionReferenceNumber + ", getId()=" + getId()
                + ", getItems()=" + getItems() + ", getRequestReferenceNumber()=" + getRequestReferenceNumber()
                + ", getReceiptNumber()=" + getReceiptNumber() + ", getCreatedAt()=" + getCreatedAt()
                + ", getUpdatedAt()=" + getUpdatedAt() + ", getPaymentScheme()=" + getPaymentScheme()
                + ", isExpressCheckout()=" + isExpressCheckout() + ", getRefundedAmount()=" + getRefundedAmount()
                + ", isCanPayPal()=" + isCanPayPal() + ", getExpiredAt()=" + getExpiredAt() + ", getStatus()="
                + getStatus() + ", getPaymentStatus()=" + getPaymentStatus() + ", getPaymentDetails()="
                + getPaymentDetails() + ", getPaymentAt()=" + getPaymentAt() + ", is3ds()=" + is3ds() + ", getBuyer()="
                + getBuyer() + ", getMerchant()=" + getMerchant() + ", getTotalAmount()=" + getTotalAmount()
                + ", getRedirectUrl()=" + getRedirectUrl() + ", getTransactionReferenceNumber()="
                + getTransactionReferenceNumber() + ", isIs3ds()=" + isIs3ds() + "]";
    }
}
