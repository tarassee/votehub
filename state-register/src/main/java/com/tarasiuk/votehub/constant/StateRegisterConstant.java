package com.tarasiuk.votehub.constant;

public final class StateRegisterConstant {

    private StateRegisterConstant() {
    }

    public static class CommonError {

        public static final String NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID = "No citizen information found for passport id '%s'";
        public static final String NO_PUBLIC_KEY_FOUND_FOR_PASSPORT_ID = "No public key found for passport id '%s'";

        private CommonError() {
        }

    }

    public static class EligibilityFilter {

        public static final String NO_FILTER = "";
        public static final String IS_CAPABLE = "isCapable";
        public static final String IS_NOT_PRISONER = "isNotPrisoner";
        public static final String IS_ADULT = "isAdult";
        public static final int ADULT_AGE = 18;

        private EligibilityFilter() {
        }

    }

}
