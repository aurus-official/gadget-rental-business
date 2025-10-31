package com.gadget.rental.payment;

import java.util.List;

public class PaymentPayloadRequest {
    private TotalAmount totalAmount;
    private Buyer buyer;
    private String requestReferenceNumber;
    private List<PaymentItem> itemList;

    public TotalAmount getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(TotalAmount totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public String getRequestReferenceNumber() {
        return requestReferenceNumber;
    }

    public void setRequestReferenceNumber(String requestReferenceNumber) {
        this.requestReferenceNumber = requestReferenceNumber;
    }

    public List<PaymentItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PaymentItem> itemList) {
        this.itemList = itemList;
    }

    static class TotalAmount {
        private double value;
        private String currency;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    static class Buyer {
        private String firstName;
        private String lastName;
        private Contact contact;

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

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        static class Contact {
            private String phone;
            private String email;

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
    }

}
