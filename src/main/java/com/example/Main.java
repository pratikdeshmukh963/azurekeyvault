package com.example;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;


public class Main {

    // for demonstration this is hardcoded, you should have it a configuration whatever your application uses
    private static final String KEY_VAULT_URL = "https://github-migration-test-kv.vault.azure.net/";
    private static final String MANAGED_IDENTITY_CLIENT_ID = "49ff3974-22d6-4e90-ada0-c6687bbde04e";

    public static void main(String[] args) {

        /**
         * Very basic example to use AzureKeyVaultUtil to get a secret from Azure Key Vault
         * If testing this locally make sure to do az login and making sure your user and
         * AVD box has access to the Azure Key Vault from Azure Portal.
         *
         * Note: If application is unable to fetch Azure Key then the application won't start.
         * So I will suggest to make sure to load required/important keys during startup and not
         * during runtime to make sure you have the required keys.
         */
        SecretClient secretClient = getSecretClient(KEY_VAULT_URL, MANAGED_IDENTITY_CLIENT_ID);
        String secretValue = secretClient.getSecret("secret-key").getValue();
        System.out.println("Secret value from Azure Key Vault: " + secretValue);

    }

    /**
     * This can be defined as a bean, for simplicity having it here
     * 
     * Note: You can use the secret client in Gradle build as well for the secrets
     * that might be required during build process. See PAVS repository on GitHub
     *
     * @param keyVaultURL Azure Key Vault URL
     * @param managedIdentityClientId User-defined managed identity created on Azure portal
     * @return {@link SecretClient}
     */
    public static SecretClient getSecretClient(final String keyVaultURL, final String managedIdentityClientId) {
        DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder()
                .managedIdentityClientId(managedIdentityClientId)
                .build();

        return new SecretClientBuilder()
                .vaultUrl(keyVaultURL)
                .credential(defaultCredential)
                .buildClient();
    }
}
