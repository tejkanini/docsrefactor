
interface UsageTab {
    title: string;
    code: string;
}

export function generateUsageTabs(usageArr: string[]): string {
    // If only one usage, just show as code block
    if (usageArr.length === 1) {
        return `## Usage\n\n\`\`\`js\n${usageArr[0]}\n\`\`\`\n`;
    }

    // Otherwise, create tabs for each usage
    const tabs: UsageTab[] = usageArr.map((code, index) => ({
        title: `Example ${index + 1}`,
        code,
    }));

    return `
## Usage

<Tabs>

${tabs.map(tab => `
  <TabItem value="javascript" label="javascript" default>

\`\`\`javascript


const { actions } = useInsight();
const ask = async (question) => {
    const { pixelReturn } = await actions.run(
        \`${tab.code}\`,
    );

    // get the message
    const message = pixelReturn[0].output.response;
    console.log(message);
};
};
\`\`\`
</TabItem>

  <TabItem value="python" label="python">
\`\`\`python
from semoss import Insight
insight = Insight(insight_id = "insight_id")
insight.run_pixel(${tab.code})
")

\`\`\`
</TabItem>

`).join('')}
</Tabs>
`;
}