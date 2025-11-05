import axios from 'axios';
import dotenv from 'dotenv';
dotenv.config({ path: './.env.local' });

export async function callRunPixel() {
	const url = 'https://workshop.cfg.deloitte.com/cfg-ai-demo/Monolith/api/engine/runPixel';
	const username = process.env.ACCESS_KEY || '';
	const password = process.env.SECRET_KEY || '';
	const basicAuth = Buffer.from(`${username}:${password}`).toString('base64');
	const headers = {
		'Content-Type': 'application/x-www-form-urlencoded',
		'Accept': 'application/json',
		'Authorization': `Basic ${basicAuth}`,
	};
	const body = new URLSearchParams({
		expression: 'help( );'
	}).toString();
	try {
	const response = await axios.post(url, body, { headers });
		return response.data;
	} catch (error) {
		console.error('Error in callRunPixel:', error);
		throw error;
	}
}
