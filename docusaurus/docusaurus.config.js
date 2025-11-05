// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config


import { themes as prismThemes } from "prism-react-renderer";

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: "SEMOSS",
  tagline: "SEMOSS Documentation",
  favicon: "img/favicon.ico",
  organizationName: "SEMOSS",
  projectName: "documentation",
  deploymentBranch: "main",
  url: "https://semoss.github.io", // Your website URL
  baseUrl: "/documentation",
  trailingSlash: false,
  onBrokenLinks: "ignore",
  onBrokenMarkdownLinks: "ignore",

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: "en",
    locales: ["en"],
  },

  presets: [
    [
      "classic",
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: "./sidebars.js",
          docItemComponent: "@theme/ApiItem",
          routeBasePath: "/",
        },
        blog: false,
        theme: {
          customCss: "./src/css/custom.css",
        },
      }),
    ],
  ],
  plugins: [
    [
      require.resolve("@easyops-cn/docusaurus-search-local"),
      {
        hashed: true,
        language: ["en"],
        removeDefaultStemmer: true,
        forceIgnoreNoIndex: true,
        indexDocs: true,
        indexPages: true,
      },
    ],
    [
      require.resolve("docusaurus-plugin-openapi-docs"),
      {
        id: "openapi-reactor",
        docsPluginId: "classic",
        config: {
          semoss: {
            specPath: "https://raw.githubusercontent.com/SEMOSS/Monolith/refs/heads/swagger-feature/swagger-ui/swagger.json",
            //specPath: "examples/semoss-api-spec.yaml",
            outputDir: "docs/ai-core-api",
            sidebarOptions: {
              groupPathsBy: "tag",
            },
            //infoTemplate: "templates/info.mustache",
          },
        },
      },

    ],
    [
      require.resolve('./plugins/docusaurus-plugin-reactor-docgen'),
      {
        txtDir: './reactorOverview', // directory containing .txt files
        outputDir: './docs/ReactorsRefactor',
      },
    ],
  ],
  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: "img/docusaurus-social-card.jpg",
      navbar: {
        title: "SEMOSS",
        logo: {
          alt: "My Site Logo",
          src: "img/logo.svg",
        },
        items: [
          {
            position: "left",
            label: "Documentation",
            items: [
              {
                label: "Introduction",
                to: "/",
              },
              {
                label: "Getting Started",
                to: "category/get-started",
              },
              {
                label: "How To",
                to: "category/how-to",
              },
              {
                label: "Advanced Installation",
                to: "category/advanced-installation",
              },
            ],
          },

          {
            label: "AI Core API",
            position: "left",
            to: "/category/ai-core-api",
          },
          {
            href: "https://github.com/facebook/docusaurus",
            label: "GitHub",
            position: "right",
          },
        ],
      },
      footer: {
        copyright: `Copyright © ${new Date().getFullYear()} SEMOSS Documentation`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
      },
      languageTabs: [
        {
          highlight: "java",
          language: "java",
          logoClass: "java",
        },
        {
          highlight: "r",
          language: "r",
          logoClass: "r",
        },
        {
          highlight: "javascript",
          language: "nodejs",
          logoClass: "nodejs",
          variant: "axios",
        },
        {
          highlight: "python",
          language: "python",
          logoClass: "python",
        },
      ],
    }),
  themes: ["docusaurus-theme-openapi-docs"],
};

export default config;
