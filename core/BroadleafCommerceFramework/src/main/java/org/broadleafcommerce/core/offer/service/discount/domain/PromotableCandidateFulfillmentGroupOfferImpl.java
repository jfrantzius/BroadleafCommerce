package org.broadleafcommerce.core.offer.service.discount.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.broadleafcommerce.core.offer.domain.CandidateFulfillmentGroupOffer;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.domain.OfferItemCriteria;
import org.broadleafcommerce.core.offer.service.type.OfferDiscountType;
import org.broadleafcommerce.money.Money;

public class PromotableCandidateFulfillmentGroupOfferImpl implements PromotableCandidateFulfillmentGroupOffer {

	private static final long serialVersionUID = 1L;
	
	protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
	protected CandidateFulfillmentGroupOffer delegate;
	protected PromotableFulfillmentGroup promotableFulfillmentGroup;
	
	public PromotableCandidateFulfillmentGroupOfferImpl(CandidateFulfillmentGroupOffer candidateFulfillmentGroupOffer, PromotableFulfillmentGroup promotableFulfillmentGroup) {
		this.delegate = candidateFulfillmentGroupOffer;
		this.promotableFulfillmentGroup = promotableFulfillmentGroup;
	}
	
	public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
		return candidateQualifiersMap;
	}

	public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap) {
		this.candidateQualifiersMap = candidateItemsMap;
	}
	
	public void computeDiscountedPriceAndAmount() {
        if (delegate.getOffer() != null && delegate.getFulfillmentGroup() != null){

            if (delegate.getFulfillmentGroup().getRetailShippingPrice() != null) {
                Money priceToUse = delegate.getFulfillmentGroup().getRetailShippingPrice();
                Money discountAmount = new Money(0);
                if ((delegate.getOffer().getApplyDiscountToSalePrice()) && (delegate.getFulfillmentGroup().getSaleShippingPrice() != null)) {
                    priceToUse = delegate.getFulfillmentGroup().getSaleShippingPrice();
                }

                if (delegate.getOffer().getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
                    discountAmount = new Money(delegate.getOffer().getValue());
                } else if (delegate.getOffer().getDiscountType().equals(OfferDiscountType.FIX_PRICE)) {
                    discountAmount = priceToUse.subtract(new Money(delegate.getOffer().getValue()));
                } else if (delegate.getOffer().getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
                    discountAmount = priceToUse.multiply(delegate.getOffer().getValue().divide(new BigDecimal("100")));
                }
                if (discountAmount.greaterThan(priceToUse)) {
                    discountAmount = priceToUse;
                }
                priceToUse = priceToUse.subtract(discountAmount);
                delegate.setDiscountedPrice(priceToUse);
            }
        }
    }
	
	public void reset() {
		delegate = null;
	}
	
	public CandidateFulfillmentGroupOffer getDelegate() {
		return delegate;
	}
	
	public Money getDiscountedPrice() {
		if (delegate.getDiscountedPrice() == null) {
            computeDiscountedPriceAndAmount();
        }
		return delegate.getDiscountedPrice();
	}

	public Offer getOffer() {
		return delegate.getOffer();
	}
	
	public PromotableFulfillmentGroup getFulfillmentGroup() {
		return promotableFulfillmentGroup;
	}
}