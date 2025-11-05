export interface PluginOptions {
  /**
   * If true, the plugin will call an API to get the list of reactors.
   * If false, the plugin will use the provided list of reactors.
   */
  useApi: boolean;

  /**
   * The API endpoint to call for reactors (required if useApi is true).
   */
  apiUrl?: string;

  /**
   * Optional headers for the API call (e.g., authentication).
   */
  apiHeaders?: Record<string, string>;

  /**
   * The list of reactors to use (required if useApi is false).
   */
  reactors?: string[];
}
