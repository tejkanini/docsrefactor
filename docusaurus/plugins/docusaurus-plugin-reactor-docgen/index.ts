import fs from 'fs';
import path from 'path';
import { generateUsageTabs } from './createUsageTabs';

export type Reactor = {
  name?: string;
  description?: string;
  requiredKeys?: string[];
  keyDescription?: string[];
  optionalKeys?: string[];
  usage?: string[];
  id?: string;
  title?: string;
  sidebar_label?: string;
  sidebar_position?: number;
  api?: string;
};

function handleSection(obj: Reactor, section: string | null, line: string) {
  switch (section) {
    case 'name':
      obj.name = line;
      break;
    case 'description':
      obj.description = line;
      break;
    case 'requiredKeys':
      obj.requiredKeys = obj.requiredKeys || [];
      obj.requiredKeys.push(line);
      break;
    case 'keyDescription':
      obj.keyDescription = obj.keyDescription || [];
      obj.keyDescription.push(line);
      break;
    case 'optionalKeys':
      obj.optionalKeys = obj.optionalKeys || [];
      obj.optionalKeys.push(line);
      break;
    case 'usage':
      obj.usage = obj.usage || [];
      obj.usage.push(line);
      break;
  }
}

export function parseReactorTxt(txt: string): Reactor {
  const lines = txt.split(/\r?\n/);
  let obj: Reactor = {};
  let section: string | null = null;
  const sectionMap: { [key: string]: string } = {
    'Reactor Name': 'name',
    'Description': 'description',
    'Required keys': 'requiredKeys',
    'Key description': 'keyDescription',
    'Optional keys': 'optionalKeys',
    'Usage': 'usage',
  };
  for (let line of lines) {
    const trimmedLine = line.trim();
    if (!trimmedLine) continue;
    const foundSection = Object.keys(sectionMap).find(key => new RegExp(`^${key}$`, 'i').test(trimmedLine));
    if (foundSection) {
      section = sectionMap[foundSection];
    } else {
      handleSection(obj, section, trimmedLine);
    }
  }
  return obj;
}

export function generateMdx(reactor: Reactor): string {
  let mdx = `---\n`;
  if (reactor.id) mdx += `id: ${reactor.id}\n`;
  if (reactor.title) mdx += `title: "${reactor.title}"\n`;
  mdx += `sidebar_label: "${reactor.sidebar_label || reactor.name}"\n`;
  if (typeof reactor.sidebar_position !== 'undefined') mdx += `sidebar_position: ${reactor.sidebar_position}\n`;
  if (reactor.description) mdx += `description: "${reactor.description}"\n`;
  mdx += `api: "${reactor.api}"\n`;
  mdx += `---\n\n# ${reactor.name}\n\n`;
//  mdx += `import Tabs from '@theme/Tabs';\n`;
//  mdx += `import TabItem from '@theme/TabItem';\n`;
  mdx += `**Description:** ${reactor.description}\n\n`;
  if (reactor.requiredKeys && reactor.requiredKeys.length) {
    mdx += `## Required Keys\n`;
    reactor.requiredKeys.forEach(k => {
      mdx += `- **${k}**\n`;
    });
    mdx += '\n';
  }
  if (reactor.keyDescription && reactor.keyDescription.length) {
    mdx += `## Key Description\n`;
    reactor.keyDescription.forEach(kd => {
      // Split at first colon to separate key and description
      const match = kd.match(/^([^:]+):\s*(.*)$/);
      if (match) {
        const key = match[1].trim();
        const desc = match[2].trim();
        mdx += `- **${key}**: ${desc}\n`;
      } else {
        mdx += `- ${kd}\n`;
      }
    });
    mdx += '\n';
  }
  if (reactor.optionalKeys && reactor.optionalKeys.length) {
    mdx += `## Optional Keys\n`;
    reactor.optionalKeys.forEach(k => {
      mdx += `- **${k}**\n`;
    });
    mdx += '\n';
  }

  if (reactor.usage && reactor.usage.length) {
    mdx += `## Usage\n`;
    mdx += '```javascript\n';
    reactor.usage.forEach(u => {
      mdx += u + '\n';
    });
    mdx += '```\n';
  }
  // Extracted usage section to ReactFile
  //if (reactor.usage && reactor.usage.length) {
  //  mdx += generateUsageTabs(reactor.usage || []);
 // }
  return mdx;
}

export interface PluginOptions {
  txtDir: string;
  outputDir: string;
}

export default function (context: any, options: PluginOptions) {
  return {
    name: 'docusaurus-plugin-reactor-docgen',
    async loadContent() {
      const txtDir = options.txtDir;
      const outputDir = options.outputDir;
      const files = fs.readdirSync(txtDir).filter(f => f.endsWith('.txt'));
      const outboundDirName = path.basename(outputDir);
      for (const file of files) {
        const txtPath = path.join(txtDir, file);
        const txt = fs.readFileSync(txtPath, 'utf-8');
        const reactor = parseReactorTxt(txt);
        let safeName = reactor.name && typeof reactor.name === 'string' ? reactor.name : path.parse(file).name;
        const fileName = `${safeName.replace(/\s+/g, '')}.mdx`;
        const outputPath = path.join(outputDir, fileName);
        const id = `${safeName.replace(/\s+/g, '').toLowerCase()}-${outboundDirName.replace(/\s+/g, '').toLowerCase()}`;
        const title = safeName.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/_/g, ' ');
        const api = `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`

        fs.writeFileSync(outputPath, generateMdx({
          ...reactor,
          name: safeName,
          id,
          title,
          api,
        }));
      }
    },
  };
}

