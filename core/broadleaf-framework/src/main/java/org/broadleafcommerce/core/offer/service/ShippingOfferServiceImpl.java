/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.core.offer.service;

import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author jfischer
 *
 */
@Service("blShippingOfferService")
public class ShippingOfferServiceImpl implements ShippingOfferService {
    
    @Resource(name="blOfferService")
    protected OfferService offerService;

    public void reviewOffers(Order order) throws PricingException {
        List<Offer> offers = offerService.buildOfferListForOrder(order);
        offerService.applyFulfillmentGroupOffersToOrder(offers, order);
    }

}
