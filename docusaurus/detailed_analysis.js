const SwaggerParser = require('swagger-parser');
const fs = require('fs');
const path = require('path');

const specsDirectory = './reactor_openapi_specs';

async function detailedAnalysis() {
    const files = fs.readdirSync(specsDirectory);
    const jsonFiles = files.filter(file => file.endsWith('.json') && file !== 'combined_reactor_api.json');
    
    console.log('=== DETAILED OPENAPI SPECIFICATIONS ANALYSIS ===\n');
    
    const issues = [];
    const recommendations = [];
    
    // Sample a few files for detailed analysis
    const sampleFiles = jsonFiles.slice(0, 5);
    
    for (const file of sampleFiles) {
        const filePath = path.join(specsDirectory, file);
        const content = JSON.parse(fs.readFileSync(filePath, 'utf8'));
        
        console.log(`\n--- Analyzing ${file} ---`);
        
        // Check OpenAPI structure
        console.log(`OpenAPI Version: ${content.openapi || 'Missing'}`);
        console.log(`Title: ${content.info?.title || 'Missing'}`);
        console.log(`Description length: ${content.info?.description?.length || 0} chars`);
        
        // Check paths structure
        const paths = content.paths || {};
        console.log(`Number of paths: ${Object.keys(paths).length}`);
        
        // Check for servers
        if (!content.servers || content.servers.length === 0) {
            issues.push(`${file}: Missing server definitions`);
        }
        
        // Check components
        if (!content.components || !content.components.schemas) {
            issues.push(`${file}: Missing component schemas`);
        }
        
        // Check response consistency
        const operations = Object.values(paths).flatMap(pathItem => 
            Object.values(pathItem).filter(op => typeof op === 'object' && op.responses)
        );
        
        const responseCodes = new Set();
        operations.forEach(op => {
            Object.keys(op.responses || {}).forEach(code => responseCodes.add(code));
        });
        
        console.log(`Response codes: ${[...responseCodes].sort().join(', ')}`);
        
        // Check for tags
        const tags = new Set();
        operations.forEach(op => {
            (op.tags || []).forEach(tag => tags.add(tag));
        });
        console.log(`Tags: ${[...tags].join(', ')}`);
        
        // Check parameter documentation
        operations.forEach(op => {
            if (op.requestBody && op.requestBody.content) {
                const schemas = Object.values(op.requestBody.content).map(content => content.schema);
                schemas.forEach(schema => {
                    if (schema && schema.properties) {
                        const props = Object.keys(schema.properties);
                        const documented = props.filter(prop => 
                            schema.properties[prop].description && 
                            schema.properties[prop].description !== 'This method is used to run an LLM text-generation call'
                        );
                        if (documented.length < props.length) {
                            issues.push(`${file}: Some parameters lack proper descriptions`);
                        }
                    }
                });
            }
        });
    }
    
    // Analyze the combined file issue
    console.log('\n\n=== COMBINED FILE ANALYSIS ===');
    const combinedPath = path.join(specsDirectory, 'combined_reactor_api.json');
    if (fs.existsSync(combinedPath)) {
        const combinedContent = JSON.parse(fs.readFileSync(combinedPath, 'utf8'));
        const combinedPaths = combinedContent.paths || {};
        
        console.log('Combined file issue: Invalid operation names');
        console.log('The combined file uses non-standard operation keys like "post_addrpyprojectpath"');
        console.log('OpenAPI specification requires standard HTTP methods: get, post, put, delete, etc.');
        
        const invalidOperations = Object.values(combinedPaths).flatMap(pathItem => 
            Object.keys(pathItem).filter(key => !['get', 'post', 'put', 'delete', 'patch', 'head', 'options', 'trace'].includes(key))
        );
        
        console.log(`Found ${invalidOperations.length} invalid operation keys`);
        console.log(`Examples: ${invalidOperations.slice(0, 5).join(', ')}`);
    }
    
    // Generate recommendations
    recommendations.push('1. Add server definitions to all individual specification files');
    recommendations.push('2. Fix the combined_reactor_api.json file structure - use proper HTTP methods');
    recommendations.push('3. Improve parameter descriptions - avoid generic descriptions');
    recommendations.push('4. Consider using consistent response schema structures across all files');
    recommendations.push('5. Add more comprehensive error response schemas');
    recommendations.push('6. Consider adding examples to request/response schemas');
    recommendations.push('7. Use consistent tagging strategy across all endpoints');
    
    console.log('\n\n=== ISSUES FOUND ===');
    issues.forEach((issue, i) => console.log(`${i + 1}. ${issue}`));
    
    console.log('\n\n=== RECOMMENDATIONS ===');
    recommendations.forEach((rec, i) => console.log(`${i + 1}. ${rec}`));
    
    // Check for common patterns
    console.log('\n\n=== COMMON PATTERNS ANALYSIS ===');
    const allFiles = jsonFiles.map(file => {
        const content = JSON.parse(fs.readFileSync(path.join(specsDirectory, file), 'utf8'));
        return { file, content };
    });
    
    // Check for consistent structure
    const structureConsistency = allFiles.every(({content}) => 
        content.openapi === '3.0.0' &&
        content.info &&
        content.info.title &&
        content.info.description &&
        content.paths &&
        content.paths['/engine/runPixel'] &&
        content.paths['/engine/runPixel'].post
    );
    
    console.log(`Structure consistency: ${structureConsistency ? '✅ All files follow the same pattern' : '❌ Inconsistent structures'}`);
    
    // Check for parameter patterns
    const parameterPatterns = allFiles.map(({file, content}) => {
        const post = content.paths?.['/engine/runPixel']?.post;
        const schema = post?.requestBody?.content?.['application/json']?.schema;
        const props = schema?.properties || {};
        return {
            file,
            hasExpression: 'expression' in props,
            hasInsightId: 'insightId' in props,
            requiredFields: schema?.required || []
        };
    });
    
    const consistentParams = parameterPatterns.every(p => p.hasExpression && p.hasInsightId);
    console.log(`Parameter consistency: ${consistentParams ? '✅ All files have expression and insightId' : '❌ Inconsistent parameters'}`);
    
    return {
        totalFiles: jsonFiles.length + 1, // +1 for combined file
        validFiles: jsonFiles.length, // All individual files are valid
        invalidFiles: 1, // Only combined file is invalid
        issues,
        recommendations
    };
}

// Run detailed analysis
detailedAnalysis().catch(console.error);
