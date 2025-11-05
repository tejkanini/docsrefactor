import type { PluginOptions } from './types';
import type { LoadContext, Plugin } from "@docusaurus/types";
import { callRunPixel } from './getGeneralReactors';

export default function pluginSemossApiExplorer(context: LoadContext, options: PluginOptions): Plugin<void> {
  return {
    name: 'docusaurus-plugin-semoss-api-explorer',
    // Add plugin hooks and logic here
  };
}
