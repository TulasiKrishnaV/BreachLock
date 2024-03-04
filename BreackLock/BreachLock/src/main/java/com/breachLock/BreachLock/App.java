package com.breachLock.BreachLock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
	public static void main(String[] args) {
		String url = "https://www.youtube.com/no-exists";
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();

			int statusCode = connection.getResponseCode();
			if (statusCode >= 400 && statusCode < 500) {
				System.out.println(statusCode);

				boolean isCustomErrorPage = isCustomErrorPage(connection);
				if (isCustomErrorPage) {
					System.out.println("Custom error page.");
				} else {
					System.out.println("Default error page.");
				}
			} else {
				System.out.println("No error detected.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isCustomErrorPage(HttpURLConnection connection) throws IOException {
		InputStream errorStream = connection.getErrorStream();
		StringBuilder response = new StringBuilder();
		if (errorStream == null) {
			return false;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {

			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String errorPageContent = response.toString();
		return errorPageContent.contains("Custom Error Page") || errorPageContent.contains("Error Page Not Found")
				|| errorPageContent.contains("<div id=\"custom-error-page\">") || errorPageContent.contains("<link");
	}
}
