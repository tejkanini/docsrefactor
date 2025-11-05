const SwaggerParser = require('swagger-parser');
const fs = require('fs');
const path = require('path');

const specsDirectory = './reactor_openapi_specs';

async function validateFile(filePath) {
    try {
        const spec = await SwaggerParser.validate(filePath);
        return { file: filePath, valid: true, spec: spec };
    } catch (error) {
        return { file: filePath, valid: false, error: error.message };
    }
}

async function validateAllSpecs() {
    const files = fs.readdirSync(specsDirectory);
    const jsonFiles = files.filter(file => file.endsWith('.json'));
    
    console.log(`Found ${jsonFiles.length} JSON files to validate...\n`);
    
    const results = [];
    
    for (const file of jsonFiles) {
        const filePath = path.join(specsDirectory, file);
        console.log(`Validating ${file}...`);
        
        const result = await validateFile(filePath);
        results.push(result);
        
        if (result.valid) {
            console.log(`✅ ${file} - Valid OpenAPI specification`);
        } else {
            console.log(`❌ ${file} - Invalid: ${result.error}`);
        }
    }
    
    console.log('\n=== VALIDATION SUMMARY ===');
    const validFiles = results.filter(r => r.valid);
    const invalidFiles = results.filter(r => !r.valid);
    
    console.log(`Valid files: ${validFiles.length}/${results.length}`);
    console.log(`Invalid files: ${invalidFiles.length}/${results.length}`);
    
    if (invalidFiles.length > 0) {
        console.log('\n=== INVALID FILES DETAILS ===');
        invalidFiles.forEach(result => {
            console.log(`\n${result.file}:`);
            console.log(`  Error: ${result.error}`);
        });
    }
    
    // Additional validation checks
    console.log('\n=== ADDITIONAL CHECKS ===');
    
    // Check for consistent OpenAPI version
    const openApiVersions = validFiles.map(r => r.spec.openapi).filter((v, i, a) => a.indexOf(v) === i);
    console.log(`OpenAPI versions used: ${openApiVersions.join(', ')}`);
    
    // Check for consistent server definitions
    const hasServers = validFiles.filter(r => r.spec.servers && r.spec.servers.length > 0);
    console.log(`Files with server definitions: ${hasServers.length}/${validFiles.length}`);
    
    // Check for consistent response structures
    const responseStructures = validFiles.map(r => {
        const paths = r.spec.paths || {};
        const operations = Object.values(paths).flatMap(pathItem => 
            Object.values(pathItem).filter(op => op.responses)
        );
        return operations.map(op => Object.keys(op.responses || {}));
    }).flat().flat();
    
    const uniqueResponseCodes = [...new Set(responseStructures)];
    console.log(`Response codes used: ${uniqueResponseCodes.sort().join(', ')}`);
    
    return results;
}

// Run validation
validateAllSpecs().catch(console.error);
