/*
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.android.iothubservices.messaging;

import com.microsoft.appcenter.espresso.Factory;
import com.microsoft.appcenter.espresso.ReportHelper;
import com.microsoft.azure.sdk.iot.android.BuildConfig;
import com.microsoft.azure.sdk.iot.android.helper.TestGroupA;
import com.microsoft.azure.sdk.iot.common.helpers.ClientType;
import com.microsoft.azure.sdk.iot.common.helpers.Rerun;
import com.microsoft.azure.sdk.iot.common.helpers.X509Cert;
import com.microsoft.azure.sdk.iot.common.tests.iothubservices.telemetry.SendMessagesTests;
import com.microsoft.azure.sdk.iot.deps.util.Base64;
import com.microsoft.azure.sdk.iot.device.InternalClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.exceptions.ModuleClientException;
import com.microsoft.azure.sdk.iot.service.BaseDevice;
import com.microsoft.azure.sdk.iot.service.auth.AuthenticationType;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collection;

@TestGroupA
@RunWith(Parameterized.class)
public class SendMessagesDeviceAndroidRunner extends SendMessagesTests
{
    static Collection<BaseDevice> identities;

    @Rule
    public Rerun count = new Rerun(3);

    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();

    public SendMessagesDeviceAndroidRunner(InternalClient client, IotHubClientProtocol protocol, BaseDevice identity, AuthenticationType authenticationType, ClientType clientType, String publicKeyCert, String privateKey, String x509Thumbprint)
    {
        super(client, protocol, identity, authenticationType, clientType, publicKeyCert, privateKey, x509Thumbprint);
    }

    //This function is run before even the @BeforeClass annotation, so it is used as the @BeforeClass method
    @Parameterized.Parameters(name = "{1}_{3}_{4}")
    public static Collection inputs() throws IOException, IotHubException, GeneralSecurityException, URISyntaxException, ModuleClientException, InterruptedException {
        X509Cert cert = new X509Cert(0,false, "TestLeaf", "TestRoot");
        String privateKey =  cert.getPrivateKeyLeafPem();
        String publicKeyCert = cert.getPublicCertLeafPem();
        String x509Thumbprint = cert.getThumbPrintLeaf();

        Collection inputs = inputsCommon(ClientType.DEVICE_CLIENT, publicKeyCert, privateKey, x509Thumbprint);

        identities = getIdentities(inputs);

        return inputs;
    }

    @AfterClass
    public static void cleanUpResources()
    {
        tearDown(identities);
    }
}