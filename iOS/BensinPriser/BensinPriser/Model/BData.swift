//
//  BData.swift
//  BensinPriser
//
//  Created by Andrian Sergheev on 2020-01-31.
//  Copyright © 2020 Andrian Sergheev. All rights reserved.
//

import Foundation

struct BCompany: Codable {
	let companyID: Int
	let companyName: String
	let logoURL: String?
	let companyURL: String?
}

struct BStation: Codable {
	let stationID: Int
	let company: BCompany
	let stationName: String

	let latitude: Double?
	let longitude: Double?

	let prices: [BFuel]
}

enum BFuleType: String, Codable {
	case diesel
	case bensin98
	case bensin95
	case ethanol85
	case gas
}

struct BFuel: Codable {
	let type: BFuleType
	let price: Double
}

struct BData: Codable {
	var stations: [BStation] = []
}

final class Test {

	init() {
		let bCompany = BCompany(companyID: 0, companyName: "BensinCompany", logoURL: "", companyURL: "")

		//		let bStation = BStation(stationID: 0,
		//								company: bCompany,
		//								stationName: "BensinStation",
		//								prices: [
		//									BFuel(type: .b98, price: 20),
		//									BFuel(type: .b95, price: 18)
		//		])

		let bData = (0...15)
			.map { BStation(stationID: $0,
							company: bCompany,
							stationName: "Name: \($0)",
				latitude: 30.3 + Double($0),
				longitude: 88.5 + Double($0),
				prices:
				//				(0...1).map { BFuel(type: .b95, price: 15 + Double($0))}
				[
					BFuel(type: .bensin98, price: 20.1),
					BFuel(type: .bensin95, price: 18.2),
					BFuel(type: .diesel, price: 14.2),
					BFuel(type: .gas, price: 13),
					BFuel(type: .ethanol85, price: 15.3)
				]
				)}

		let jsonEncoder = JSONEncoder()
		let jsonData = try! jsonEncoder.encode(bData)
		let json = String(data: jsonData, encoding: String.Encoding.utf8)

		print(json!)
	}
}
