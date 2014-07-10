/*
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.payment.core.sm;

import org.killbill.automaton.OperationResult;
import org.killbill.billing.osgi.api.OSGIServiceRegistration;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentApiException;
import org.killbill.billing.payment.core.PaymentProcessor;
import org.killbill.billing.payment.dispatcher.PluginDispatcher;
import org.killbill.billing.retry.plugin.api.PaymentControlPluginApi;
import org.killbill.commons.locker.GlobalLocker;

public class RetryChargebackOperationCallback extends RetryOperationCallback {

    public RetryChargebackOperationCallback(final GlobalLocker locker, final PluginDispatcher<OperationResult> paymentPluginDispatcher, final RetryablePaymentStateContext paymentStateContext, final PaymentProcessor paymentProcessor, final OSGIServiceRegistration<PaymentControlPluginApi> paymentControlPluginRegistry) {
        super(locker, paymentPluginDispatcher, paymentStateContext, paymentProcessor, paymentControlPluginRegistry);
    }

    @Override
    protected Payment doCallSpecificOperationCallback() throws PaymentApiException {
        return paymentProcessor.createChargeback(retryablePaymentStateContext.isApiPayment(),
                                                       retryablePaymentStateContext.getAttemptId(),
                                                       retryablePaymentStateContext.getAccount(),
                                                       retryablePaymentStateContext.getPaymentId(),
                                                       retryablePaymentStateContext.getPaymentTransactionExternalKey(),
                                                       retryablePaymentStateContext.getAmount(),
                                                       retryablePaymentStateContext.getCurrency(),
                                                       false,
                                                       retryablePaymentStateContext.getCallContext(),
                                                       retryablePaymentStateContext.getInternalCallContext());
    }
}
